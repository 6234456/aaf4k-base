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
            println(account.copy(value = 1234567 + it * 200))
        }
    }

    @Test
    fun toString1() {
    }
}