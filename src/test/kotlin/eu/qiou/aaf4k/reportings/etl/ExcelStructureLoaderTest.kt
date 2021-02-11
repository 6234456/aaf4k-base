package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.reportings.base.AccountingFrame
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit

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
/*
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

        val d = (reporting2.update(dataLoader.load()) as CollectionAccount)
            .flattenAll().filter { it is CollectionAccount }.map { it.id - 10000 to it.decimalValue * it.reportingType.sign }.toMap() +
                (reporting2.nullify().update(dataLoader2.load()) as CollectionAccount)
                    .flattenAll().filter { it is CollectionAccount }.map { it.id + 80000 to it.decimalValue * it.reportingType.sign }.toMap() + mapOf(
            "YYYY" to 2019,
            "entity" to "Demo GmbH",
            "unit" to "欧元",
            "MM" to 12,
            "DD" to 31,
            "isAG" to false
        )

        ExcelReportingTemplate(
            tpl = this.javaClass.classLoader.getResource("data/cn/CAS.xlsx").path,
            fmt = "%.0f"
        ).export(
            d,
            path = "trail3.xlsx",
            filter = { s -> s.sheetName.length == 2 }
        )
    }

    @Test
    fun trail1() {
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

        Reporting(reporting2.update(dataLoader.load()) as CollectionAccount).toXl(
            "trail1.xlsx",
            locale = Locale.GERMANY
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


        println(reporting2.findAccountByID(17000L)!!.superAccounts)

        reporting2.chronoToXl(
            mutableMapOf(
                TimeParameters.forYear(2019) to mapOf(996L to 2.34),
                TimeParameters.forYear(2018) to mapOf(996L to 2.34)
            ), "trail2.xlsx", shtNameOverview = "overview"
        )


    }
    */

}