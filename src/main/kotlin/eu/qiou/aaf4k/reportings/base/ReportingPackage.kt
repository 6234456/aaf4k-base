package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.io.ExcelUtil
import eu.qiou.aaf4k.util.template.Template
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import java.util.*

class ReportingPackage(
    val cover: Reporting,
    val components: Map<Entity, Reporting>,
    val currencies: Map<Long, CurrencyUnit> = mapOf()
) {
    fun toXl(
        path: String,
        t: Template.Theme = Template.Theme.DEFAULT,
        locale: Locale = Locale.getDefault(),
        shtNameOverview: String = "Overview",
        shtNameAdjustments: String = "Adjustments",
        accountIdFXDiff: Long? = null,
        targetCurrency: CurrencyUnit = cover.unit as CurrencyUnit,
        timeParameters: TimeParameters = cover.timeParameters, override: Map<Long, Double> = mapOf()
    ) {
        val colStart = 2
        var cnt = 0

        val data: MutableMap<Int, Map<Long, String>> = mutableMapOf()

        cover.toXl(path, t, locale, shtNameOverview, shtNameAdjustments, components)

        components.forEach { (k, v) ->
            val overview = "${String.format("%03d", k.id)}_${k.abbreviation}_$shtNameOverview"
            val adj = "${String.format("%03d", k.id)}_${k.abbreviation}_$shtNameAdjustments"
            val (_, data1) = v.toXl(
                path, t, locale, overview, adj,
                accountIdFXDiff = accountIdFXDiff,
                sourceCurrency = currencies.getOrDefault(k.id, targetCurrency),
                timeParameters = timeParameters,
                targetCurrency = targetCurrency,
                override = override
            )

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