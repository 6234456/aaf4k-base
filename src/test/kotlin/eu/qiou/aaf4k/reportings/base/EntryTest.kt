package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import org.junit.Test
import java.util.*

class EntryTest {

    @Test
    fun toDataMap() {
        val reporting = AccountingFrame
            .inflate(
                10004L, "SKR4",
                this.javaClass.classLoader.getResourceAsStream("data/de/de_hgb_2018.txt"),
                unit = CurrencyUnit("EUR", 4), decimalPrecision = 3,
                displayUnit = CurrencyUnit("EUR", 4),
                timeParameters = TimeParameters.forYear(2019)
            )
            .toReporting()

        reporting.prepareConsolidation(locale = Locale.GERMANY)

        val category = Category("Demo", "a demo category", reporting)
        val entry = Entry("trail", category)

        entry.add(111L, 12.2391)
        val a = (reporting.search(112L) as Account).copy(
            value = 785000, decimalPrecision = 2,
            unit = CurrencyUnit(currency = "CNY"),
            timeParameters = TimeParameters(2019, 12, 31)
        )
        entry.add(a)
        entry.balanceWith(634L)

        println(entry)

        println(category.toJSON())

        entry.unregister()

        //    println(category.toJSON())

        println(reporting.categories.map { it.name })


        reporting.replace(111L, CollectionAccount(111L, "replaced").apply {
            add(Account(11112L, "trails", 1231L))
            add(Account(11113L, "trails2", 1231L))
        })

        println(reporting.findAccountByID(111L))



    }
}