package eu.qiou.aaf4k.finance

import eu.qiou.aaf4k.reportings.plan.Annunity
import eu.qiou.aaf4k.reportings.plan.PresentValue
import eu.qiou.aaf4k.util.roundUpTo
import org.junit.Test

class ZuschlagskalkulationTest {

    @Test
    fun forProduct() {
        val k1 = Kostenstelle(1L, "1")
        val k2 = Kostenstelle(2L, "2")
        val v = Zuschlagskalkulation(
            mapOf(Kostenstelle.GENERAL_COST to 45_000.0), mapOf(Kostenstelle.GENERAL_COST to 30_000.0),
            mapOf(k1 to 60_000.0, k2 to 10_000.0),
            mapOf(k1 to 360_000.0, k2 to 520_000.0),
            50_000.0, 25_530.0, 93_700.0, 2
        ).forProduct(
            mapOf(Kostenstelle.GENERAL_COST to 140.0), mapOf(k1 to 200.0, k2 to 120.0), 30.0
        )
        println(v)
    }

    @Test
    fun trail() {
        val a = Annunity(5, 0.1).payment(1000.0)
        val p = PresentValue(listOf(29.77, 31.08, 32.46, 33.88, 35.39), 0.0555, 36.94)
        println(p.presentValue.map { it.roundUpTo(4) })
        println(p.value)
    }
}