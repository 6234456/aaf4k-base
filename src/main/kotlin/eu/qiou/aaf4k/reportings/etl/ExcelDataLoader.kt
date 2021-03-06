package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.util.io.ExcelUtil
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row

class ExcelDataLoader(
    var path: String, var sheetIndex: Int = 0, var sheetName: String? = null, var keyCol: Int = 1,
    var valCol: Int = 2, var hasHeading: Boolean = false
) : DataLoader {
    private val reg2 = Regex("""\s+""")

    override fun load(): MutableMap<Long, Double> {
        val res: MutableMap<Long, Double> = mutableMapOf()
        val f:(Row) -> Unit = {
            if (! (hasHeading && it.rowNum < 1)){
                val idCol = keyCol - 1
                val valueCol = valCol - 1


                val cellKey = it.getCell(idCol)
                val cellVal = it.getCell(valueCol)

                if (cellKey != null && cellVal != null) {
                    val c1 =
                        if (cellKey.cellTypeEnum == CellType.STRING && cellKey.stringCellValue.isNotBlank()) {
                            reg2.split(cellKey.stringCellValue, 2)[0].toLong()
                        } else {
                            cellKey.numericCellValue.toLong()
                        }
                    val c2 = try {
                        cellVal.numericCellValue
                    } catch (e: Exception) {
                        0.0
                    }

                    res[c1] = c2
                }
            }
        }

        ExcelUtil.loopThroughRows(path=path, sheetIndex = sheetIndex, sheetName = sheetName, callback = f)

        return res
    }
}