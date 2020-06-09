package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.reportings.base.Account
import eu.qiou.aaf4k.reportings.base.AccountingFrame
import eu.qiou.aaf4k.reportings.base.CollectionAccount
import eu.qiou.aaf4k.reportings.base.Reporting
import eu.qiou.aaf4k.util.template.Template
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import org.junit.Test

class ExcelStructureLoaderTest {

    val loader = ExcelStructureLoader("Mapping.xlsx")
    val dataLoader = ExcelDataLoader("Mapping.xlsx", keyCol = 2, valCol = 3)
    val dataLoader2 = ExcelDataLoader("Mapping.xlsx", keyCol = 2, valCol = 4)
    val reporting2 = AccountingFrame
        .inflate(
            10004L, "SKR4",
            this.javaClass.classLoader.getResourceAsStream("data/cn/cn_cas_2018.txt"),
            unit = CurrencyUnit("EUR", 2), decimalPrecision = 2,
            displayUnit = CurrencyUnit("EUR", 2),
            timeParameters = TimeParameters.forYear(2019)
        )
        .toReporting()

    @Test
    fun trial() {
        reporting2.map {
            when (it) {
                is Account -> it.copy(id = it.id + 10000)
                is CollectionAccount -> it.copy(id = it.id + 10000)
                else -> throw Exception("")
            }
        }

        loader.load().forEach { account ->
            reporting2.replace(account)
        }

        reporting2.chronoToXl(
            mutableMapOf(
                TimeParameters.forYear(2019) to dataLoader.load(),
                TimeParameters.forYear(2018) to dataLoader2.load()
            ), "trail2.xlsx", shtNameOverview = "overview"
        )
    }

    @Test
    fun load() {

        reporting2.map {
            when (it) {
                is Account -> it.copy(id = it.id + 10000)
                is CollectionAccount -> it.copy(id = it.id + 10000)
                else -> throw Exception("")
            }
        }

        loader.load().forEach { account ->
            reporting2.replace(account)
        }


        val report1 = reporting2
        val report2 = reporting2

        val d1 = dataLoader.load()
        val d2 = dataLoader2.load()

        val l = d1.keys + d2.keys


        //  println(reporting2)

        Reporting(report1.update(d1).shorten(whiteList = l) as CollectionAccount).toXl(
            "trail1.xlsx",
            Template.Theme.BLACK_WHITE,
            shtNameOverview = "2019",
            shtNameAdjustment = "adj2019"
        )
        Reporting(report2.update(d2).shorten(whiteList = l) as CollectionAccount).toXl(
            "trail1.xlsx",
            Template.Theme.BLACK_WHITE,
            shtNameOverview = "2018",
            shtNameAdjustment = "adj2018"
        )

        reporting2.chronoToXl(
            mutableMapOf(
                TimeParameters.forYear(2019) to mapOf(996L to 2.34),
                TimeParameters.forYear(2018) to mapOf(996L to 2.34)
            ), "trail1.xlsx", shtNameOverview = "overview"
        )

    }

    @Test
    fun getPath() {
        (reporting2.map {
            when (it) {
                is Account -> it.copy(id = it.id + 10000)
                is CollectionAccount -> it.copy(id = it.id + 10000)
                else -> throw Exception("")
            }
        }.search(17000)!!).apply {

            this as CollectionAccount
            println(allParentAccounts().map { "${it.id} ${it.name}" })
            println(allParents().map { "${it.id} ${it.name}" })
            println(superAccounts[0].id)

        }

        loader.load().forEach { account ->
            reporting2.replace(account)
        }

        println(reporting2.shorten(whiteList = listOf(1200, 8120)))

        val extendedList: Set<Long> = listOf(1200L, 8120L).fold(setOf()) { acc, l ->
            acc + (reporting2.search(l)?.let {
                it.allParentAccounts().map { it.id }
            } ?: setOf())
        }

        println(extendedList)

        println(reporting2.findAccountByID(17000L)!!.superAccounts)

        reporting2.chronoToXl(
            mutableMapOf(
                TimeParameters.forYear(2019) to mapOf(996L to 2.34),
                TimeParameters.forYear(2018) to mapOf(996L to 2.34)
            ), "trail2.xlsx", shtNameOverview = "overview"
        )


    }
}