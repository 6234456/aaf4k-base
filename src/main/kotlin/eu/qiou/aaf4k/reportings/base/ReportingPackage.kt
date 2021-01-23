package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.io.ExcelUtil
import eu.qiou.aaf4k.util.template.Template
import java.util.*

class ReportingPackage(val cover: Reporting, val components: Map<Entity, Reporting>) {
    fun toXl(
        path: String,
        t: Template.Theme = Template.Theme.DEFAULT,
        locale: Locale = Locale.getDefault(),
        shtNameOverview: String = "Overview",
        shtNameAdjustments: String = "Adjustments"
    ) {
        val colStart = 2
        var cnt = 0

        val data: MutableMap<Int, Map<Long, String>> = mutableMapOf()

        cover.toXl(path, t, locale, shtNameOverview, shtNameAdjustments, components)

        components.forEach { (k, v) ->
            val overview = "${String.format("%03d", k.id)}_${k.abbreviation}_$shtNameOverview"
            val adj = "${String.format("%03d", k.id)}_${k.abbreviation}_$shtNameAdjustments"
            val (_, data1) = v.toXl(path, t, locale, overview, adj)

            data[colStart + cnt++] = data1
        }

        val (sht, _) = ExcelUtil.getWorksheet(path, sheetName = shtNameOverview)

        data.forEach { (i, d) ->
            ExcelUtil.unload(d, { if (ExcelUtil.digitRegex.matches(it)) it.toDouble().toLong() else -1 }, 0, i, { false }, { c, v ->
                c.cellFormula = v
            }, sht)
        }

        ExcelUtil.saveWorkbook(path, sht.workbook)
    }
}