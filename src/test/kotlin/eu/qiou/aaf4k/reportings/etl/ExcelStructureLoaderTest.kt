package eu.qiou.aaf4k.reportings.etl

import eu.qiou.aaf4k.reportings.base.Account
import eu.qiou.aaf4k.reportings.base.AccountingFrame
import eu.qiou.aaf4k.reportings.base.CollectionAccount
import eu.qiou.aaf4k.reportings.base.Reporting
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import org.junit.Test

class ExcelStructureLoaderTest {

    val loader = ExcelStructureLoader("Mapping.xlsx")
    val dataLoader = ExcelDataLoader("Mapping.xlsx", keyCol = 2, valCol = 3)
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
    fun load() {

        reporting2.map {
            when (it) {
                is Account -> it.copy(id = it.id + 10000)
                is CollectionAccount -> it.copy(id = it.id + 10000)
                else -> throw Exception("")
            }
        }


        val report1 = loader.load().forEach { account ->
            reporting2.replace(account)
        }
        //  println(reporting2)

        Reporting(reporting2.update(dataLoader.load()).shorten() as CollectionAccount).toXl("trail.xlsx")

    }

    @Test
    fun getPath() {
    }
}