package eu.qiou.aaf4k.reportings.base

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import kotlin.test.assertFails

class AccountingFrameTest {

    @Test
    fun toReporting() {
        val reporting = AccountingFrame
            .inflate(10004L, "SKR4", this.javaClass.classLoader.getResourceAsStream("data/cn/cn_cas_2018.txt"))
            .toReporting()

        reporting.prepareConsolidation(Locale.CHINESE)

        val a = reporting.categoryPayablesReceivabelsCons!!.nextEntryIndex

        assertFails {
            reporting.categoryPayablesReceivabelsCons!!.add(
                mapOf(
                    3100L to 100.0,
                    1005L to -101.0
                ), "偿还短期借款"
            )
        }
        assertEquals(a, reporting.categoryPayablesReceivabelsCons!!.nextEntryIndex)

        reporting.categoryPayablesReceivabelsCons!!.add(
            mapOf(
                3100L to 100.0,
                6400L to -100.0
            ), "偿还短期借款"
        )

        reporting.categoryPayablesReceivabelsCons!!.summarizeResult()

        assertEquals(true, reporting.generate().findAccountByID(5600)!!.decimalValue == -100.0)

    }
}