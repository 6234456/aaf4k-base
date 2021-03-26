package eu.qiou.aaf4k.finance

import eu.qiou.aaf4k.util.mkString
import org.junit.Test

class LeistungsmatrixTest {

    @Test
    fun stufenleiterVerfahren() {
        val p = Leistungsmatrix(
            arrayOf(
                arrayOf(0.0, 200.0, 100.0, 100.0),
                arrayOf(150.0, 0.0, 250.0, 400.0),
                arrayOf(0.0, 0.0, 70.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 62.0)),
            costCenters = arrayOf(
                Kostenstelle(1L, "K1"),
                Kostenstelle(2L, "K2"),
                Kostenstelle(3L, "K3", false),
                Kostenstelle(4L, "K4", false)
            ),
            costs = arrayOf(60.0, 48.0, 70.0, 62.0)
        ).stufenleiterVerfahren()

        p.forEach {
            println("${it.first.name} : ${it.second.toList().mkString()}")
        }
    }
}