package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import eu.qiou.aaf4k.util.unit.UnitScalar
import org.junit.Test

class AccountTest {
    val account = Account(
        123, "Demo", 12345600, decimalPrecision = 2,
        unit = CurrencyUnit(UnitScalar.UNIT, "CNY"),
        timeParameters = TimeParameters.forYear(2020),
        displayUnit = CurrencyUnit(UnitScalar.UNIT, "EUR")
    )

    @Test
    fun nullify() {
        (1..100L).forEach {
            //            println(account.copy(value = 1234567 + it * 200))
        }
    }

    @Test
    fun toString1() {
        val acc = Account(1231, "Trail1", 121212)
        val acc1 = Account(1233, "Trail3", 121212)
        val acc2 = Account(12335, "Trail4", 1212124)
        val acc0 = CollectionAccount(121, "demo").apply {
            add(CollectionAccount(1232, "Trail2", isStatistical = true).apply {
                add(acc)
                add(acc2)
            })
            add(acc1)
        }
        Reporting(CollectionAccount(123, "Trail").apply {
            add(acc)
            add(acc0)
            add(acc2)
        }).toXl("Demo2.xlsx")
    }

    @Test
    fun demo() {
        val a = AccountingFrame
            .inflate(
                10004L, "SKR4",
                this.javaClass.classLoader.getResourceAsStream("data/de/de_hgb_2018.txt"),
                // this.javaClass.classLoader.getResourceAsStream("data/de/de_hgb_2018.txt"),
                unit = CurrencyUnit("EUR", 2), decimalPrecision = 2,
                displayUnit = CurrencyUnit("EUR", 2),
                timeParameters = TimeParameters.forYear(2020)
            )
            .toReporting().findAccountByID(8L) as CollectionAccount

        a.replace(CollectionAccount(881L, "asdf", isStatistical = true))

        println(a)
    }
}