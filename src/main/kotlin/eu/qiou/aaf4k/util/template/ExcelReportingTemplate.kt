package eu.qiou.aaf4k.util.template

import eu.qiou.aaf4k.util.io.ExcelUtil
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Sheet

class ExcelReportingTemplate(
    private val tpl: String,
    prefix: String = "[", affix: String = "]",
    private val shtName: String? = null, private val shtIndex: Int = 0, fmt: String = "%.2f"
) {
    private val engine = TemplateEngine(prefix, affix, fmt)

    fun export(
        data: Map<*, *>,
        path: String,
        filter: (Sheet) -> Boolean = {
            if (shtName != null) it.sheetName == shtName else it.workbook.getSheetIndex(it) == shtIndex
        },
        fillFormula: Boolean = false
    ) {
        val (wb, ips) = ExcelUtil.getWorkbook(tpl)
        val d = data.map { it.key.toString() to it.value!! }.toMap()
        val toRemove: MutableList<Int> = mutableListOf()

        wb.sheetIterator().forEach { sht ->
            if (filter(sht)) {
                sht.rowIterator().forEach { x ->
                    x.cellIterator().forEach {
                        if (it.cellTypeEnum == CellType.STRING) {
                            if (engine.containsTemplate(it.stringCellValue)) {
                                val v = engine.compile(it.stringCellValue)(d)
                                if (fillFormula) {
                                    it.cellFormula = v
                                } else {
                                    try {
                                        it.setCellValue(v.toDouble())
                                    } catch (e: Exception) {
                                        it.setCellValue(v)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                toRemove.add(wb.getSheetIndex(sht))
            }
        }

        toRemove.reverse()
        toRemove.forEach {
            wb.removeSheetAt(it)
        }

        wb.forceFormulaRecalculation = true
        ExcelUtil.saveWorkbook(path, wb)
        wb.close()
        ips.close()
    }
}