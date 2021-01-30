package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.reportings.base.Account
import eu.qiou.aaf4k.reportings.base.AccountingFrame
import eu.qiou.aaf4k.reportings.base.CollectionAccount
import eu.qiou.aaf4k.reportings.base.Reporting
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import org.junit.Test

class ThreeColumnStructureLoaderTest {
    val p = "合并底稿4.xlsx"
    val loader = ThreeColumnStructureLoader("Consolidation_2020.xlsx", sep = " ", sheetName = "mapping")
    val loader_ifrs = ThreeColumnStructureLoader("Consolidation_2020.xlsx", sep = " ", sheetName = "ifrs")
    val de2019 =
        ExcelDataLoader("Consolidation_2020.xlsx", keyCol = 1, valCol = 3, sheetName = "DE", hasHeading = false)
    val de2020 =
        ExcelDataLoader("Consolidation_2020.xlsx", keyCol = 1, valCol = 4, sheetName = "DE", hasHeading = false)
    val cz2019 =
        ExcelDataLoader("Consolidation_2020.xlsx", keyCol = 1, valCol = 3, sheetName = "CZ", hasHeading = false)
    val cz2020 =
        ExcelDataLoader("Consolidation_2020.xlsx", keyCol = 1, valCol = 4, sheetName = "CZ", hasHeading = false)
    val reporting2 = AccountingFrame
        .inflate(
            10004L, "SKR4",
            this.javaClass.classLoader.getResourceAsStream("data/de/de_hgbCN_2021.txt"),
            // this.javaClass.classLoader.getResourceAsStream("data/de/de_hgb_2018.txt"),
            unit = CurrencyUnit("EUR", 2), decimalPrecision = 2,
            displayUnit = CurrencyUnit("EUR", 2),
            timeParameters = TimeParameters.forYear(2020)
        )
        .toReporting()
    val ifrs = AccountingFrame
        .inflate(
            10006L, "CAS",
            this.javaClass.classLoader.getResourceAsStream("data/cn/cn_cas_2018.txt"),
            // this.javaClass.classLoader.getResourceAsStream("data/de/de_hgb_2018.txt"),
            unit = CurrencyUnit("EUR", 2), decimalPrecision = 2,
            displayUnit = CurrencyUnit("EUR", 2),
            timeParameters = TimeParameters.forYear(2020)
        )
        .toReporting()

    @Test
    fun trail2() {
        println(loader_ifrs.load())

        //ifrs.mountStructure(loader_ifrs)
    }

    @Test
    fun load() {

        reporting2.map {
            when (it) {
                is Account -> it.copy(id = it.id + 9000000)
                is CollectionAccount -> it.copy(id = it.id + 9000000)
                else -> throw Exception("")
            }
        }

        val reporting0 = reporting2.deepCopy() as Reporting

        loader.load().forEach { account ->
            reporting2.replace(account)
        }

        reporting2.map {
            when (it) {
                is Account -> it.copy(name = reporting0.search(it.id)?.name ?: it.name)
                is CollectionAccount -> it.copy(name = reporting0.search(it.id)?.name ?: it.name)
                else -> throw Exception("")
            }
        }

        println(reporting2.findAccountByID(9000003))

        reporting2.chronoToXl(
            mutableMapOf(
                TimeParameters.forYear(2020) to de2020.load(),
                TimeParameters.forYear(2019) to cz2020.load(),
                TimeParameters.forYear(2018) to de2019.load(),
                TimeParameters.forYear(2017) to cz2019.load()
            ), "DE.xlsx", shtNameOverview = "overview"
        )
    }


}