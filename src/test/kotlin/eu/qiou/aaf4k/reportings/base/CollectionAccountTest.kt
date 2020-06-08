package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.io.FxProvider
import eu.qiou.aaf4k.util.io.toAccount
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import eu.qiou.aaf4k.util.unit.ForeignExchange
import eu.qiou.aaf4k.util.withinTolerance
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class CollectionAccountTest {
    init {
        ForeignExchange.source = object : FxProvider() {
            override fun fetchFxFromSource(target: ForeignExchange): Double {
                if (target.functionalCurrency == Currency.getInstance("CNY"))
                    return 0.7

                return 1.0
            }
        }
    }

    private val collectionAccount =
        CollectionAccount(
            123, "equity", reportingType = ReportingType.EQUITY,
            unit = CurrencyUnit("CNY"), displayUnit = CurrencyUnit("EUR"),
            timeParameters = TimeParameters.realTime()
        )
            .apply {
                addAll(
                    listOf(
                        Account(1231, "capital", 3121200, decimalPrecision = 4),
                        Account(1232, "capital reserve", 3121200, decimalPrecision = 2),
                        CollectionAccount(1233, "other reserves").apply {},
                        CollectionAccount(1234, "OCI", isStatistical = true).apply {
                            add(
                                Account(12341, "Translation Difference", 1000000L)
                            )
                        },
                        Account(
                            1235, "statuary reserve", 3121200, decimalPrecision = 2,
                            isStatistical = true
                        )
                    )
                )
            }.toJSON().toAccount() as CollectionAccount

    @Test
    fun getSubAccounts() {
        assertEquals(true, collectionAccount.displayValue.withinTolerance(0.0001, (312.12 + 31212) * 0.7))
        assertEquals(true, collectionAccount.search(1233L)!!.decimalValue == 0.00)
        assertEquals(
            true, (collectionAccount.deepCopy().update(mapOf(12341L to 20000.0)) as CollectionAccount)
                .search(12341L)!!.decimalValue == 30000.00
        )
        assertEquals(true, collectionAccount.find { it.id == 12341L }!!.decimalValue == 10000.00)

        //after update the structure force update the cache
        (collectionAccount.search(1234L) as CollectionAccount).add(Account(12342L, "demo", 3))
        collectionAccount.toUpdate = true
        assertEquals(true, collectionAccount.search(12342L)!!.value == 3L)

        println((collectionAccount.nullify().update(mapOf(12341L to 20000.0)) as CollectionAccount).shorten())
    }

    @Test
    fun removeAccount() {
        val superAccount = CollectionAccount(123L, "s1")
        val sub1 = Account(1230L, "trial", 12121)
        val sub2 = Account(1231L, "trial", 12121)
        superAccount.add(CollectionAccount(0L, "demo").apply {
            add(sub1)
            add(sub2)
        })

        println(superAccount)

        superAccount.replace(
            1231L,
            Account(1245L, "replace", 0L)
        )

        println(superAccount)

    }
}