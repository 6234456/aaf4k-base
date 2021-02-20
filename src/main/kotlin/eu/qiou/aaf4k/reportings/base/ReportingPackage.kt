package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.io.ExcelUtil
import eu.qiou.aaf4k.util.io.JSONable
import eu.qiou.aaf4k.util.merge
import eu.qiou.aaf4k.util.mkString
import eu.qiou.aaf4k.util.template.Template
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import eu.qiou.aaf4k.util.unit.ForeignExchange
import org.apache.poi.ss.usermodel.Sheet
import java.util.*

class ReportingPackage(
    val cover: Reporting,
    val components: Map<Entity, Reporting>,
    val currencies: Map<Long, CurrencyUnit> = mapOf(), // id of the entity to the functional currency of the entity
    val accountIdFXDiff: Long? = null,
    val targetCurrency: CurrencyUnit = cover.unit as CurrencyUnit,
    val timeParameters: TimeParameters = cover.timeParameters,
    val override: Map<Long, Double> = mapOf(), // the equity account which to be overriden with historical rate
    val currencyProfile: Map<ForeignExchange, Double> = mapOf() // evaluate with other exchange rate than the official rate
) : JSONable {
    override fun toJSON(): String {
        return """{"cover":${cover.toJSON()}, "components":${
        components.toList()
            .map { "{\"entity\":${it.first.toJSON()}, \"reporting\":${it.second.toJSON()}}" }
            .mkString()}, "currencies":${currencies.toList().map {
            "{\"id\":${it.first}, \"currency\":${it.second.toJSON()}}"
        }.mkString()}, "accountIdFXDiff": $accountIdFXDiff, "targetCurrency": ${targetCurrency
            .toJSON()}, "timeParameters": ${timeParameters.toJSON()}, "override":${
        override.toList().map { "{\"id\":${it.first}, \"value\":${it.second}}" }.mkString()
        }, "currencyProfile": ${currencyProfile.toList().map {
            "{\"foreignExchange\":${it.first.toJSON()}, \"value\":${it.second}}"
        }.mkString()}
        }"""
    }

    // SuSa vor der Anpassung
    fun raw(): Map<Long, Double> {
        return components.asSequence().fold(mapOf<Long, Double>()) { acc, seq ->
            acc.merge(
                seq.value.fx(
                    accountIdFXDiff = accountIdFXDiff!!,
                    sourceCurrency = currencies.getOrDefault(seq.key.id, targetCurrency),
                    timeParameters = timeParameters,
                    targetCurrency = targetCurrency,
                    override = override
                ).toDataMap()
            )
        }
    }

    // Summebilanz vor der Anpassung
    fun toReporting(): Reporting {
        return Reporting(cover.copy().update(data = raw()) as CollectionAccount).copyCategoriesFrom(cover)
    }

    fun toXl(
        path: String,
        t: Template.Theme = Template.Theme.DEFAULT,
        locale: Locale = Locale.getDefault(),
        shtNameOverview: String = "Overview",
        shtNameAdjustments: String = "Adjustments"
    ): Pair<Sheet, Map<Long, String>> {
        val colStart = 2
        var cnt = 0

        val oldProfile = ForeignExchange.override
        ForeignExchange.override = currencyProfile
        // key: TargetCol in the overview-sht
        // value: dict with address
        val data: MutableMap<Int, Map<Long, String>> = mutableMapOf()

        val res = cover.toXl(path, t, locale, shtNameOverview, shtNameAdjustments, components)

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

        ForeignExchange.override = oldProfile
        return res
    }
}