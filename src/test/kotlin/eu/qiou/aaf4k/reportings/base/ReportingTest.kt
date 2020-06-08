package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import org.junit.Test

class ReportingTest {

    val collectionAccount = CollectionAccount(0L, "Demo")
    val reporting = Reporting(collectionAccount)

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
    fun replace() {
        reporting.add(CollectionAccount(1L, "1").apply {
            add(Account(11L, "demo11"))
            add(Account(12L, "demo12", 2L))
        })

        reporting.replace(12L, CollectionAccount(123L, "repl").apply {
            add(Account(23L, "demo1"))
            add(Account(24L, "demo1"))
        })

        reporting.removeRecursively(24L)

        println(reporting)

    }

    @Test
    fun replace1() {
        reporting2.map {
            if (it is Account) {
                val account = if (it.id in 1000..3000)
                    it.copy(id = it.id + 10000, value = it.value + 10000)
                else
                    it.copy(id = it.id + 10000)
                account
            } else if (it is CollectionAccount)
                it.copy(id = it.id + 10000)
            else
                throw Error("")
        }

        println(reporting2.shorten().shorten())
        println(reporting2.findAccountByID(12000)?.displayValue)
    }

    @Test
    fun search() {
    }

    @Test
    fun shorten() {
    }
}