package eu.qiou.aaf4k.finance

import eu.qiou.aaf4k.util.mkString
import eu.qiou.aaf4k.util.sortedWithOrderList
import org.junit.Test

class LeistungsmatrixTest {

    @Test
    fun stufenleiterVerfahren() {
        val p = Leistungsmatrix(
            arrayOf(
                arrayOf(20.0, 10.0, 25.0, 70.0, 130.0),
                arrayOf(11.0, 2.0, 0.0, 15.0, 74.0),
                arrayOf(25.0, 28.0, 0.0, 29.0, 38.0),
                arrayOf(0.0, 0.0, 0.0, 500.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 400.0)
            ),
            arrayOf(
                Kostenstelle(1L, "K1"),
                Kostenstelle(2L, "K2"),
                Kostenstelle(3L, "K3"),
                Kostenstelle(4L, "K4", false),
                Kostenstelle(5L, "K5", false)
            ),
            arrayOf(40.0, 80.0, 60.0, 80.0, 120.0)
        )

        p.stufenleiterVerfahren().forEach { println("${it.first.name} : ${it.second.toList().mkString()}") }
        println("----------")
        p.gleichungsVerfahren().forEach { println("${it.first.name} : ${it.second.toList().mkString()}") }
        println("----------")
        p.anbauVerfahren().forEach { println("${it.first.name} : ${it.second.toList().mkString()}") }
        println("----------")

        val p1 = Leistungsmatrix(
            arrayOf(
                arrayOf(0.0, 0.2, 0.8, 0.0, 0.0),
                arrayOf(0.6, 0.0, 0.15, 0.1, 0.15),
                arrayOf(0.0, 0.0, 0.0, 0.4, 0.6),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0),
                arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)
            ),
            arrayOf(
                Kostenstelle(1L, "K1"),
                Kostenstelle(2L, "K2"),
                Kostenstelle(3L, "K3"),
                Kostenstelle(4L, "KA", false),
                Kostenstelle(5L, "KB", false)
            ),
            arrayOf(50.0, 100.0, 50.0, 300.0, 500.0)
        )

        p1.stufenleiterVerfahren().forEach {
            println("${it.first.name} : ${it.second.toList().mkString()}")
        }

        println("----------")

        p1.gleichungsVerfahren().forEach {
            println("${it.first.name} : ${it.second.toList().mkString()}")
        }
    }

    @Test
    fun sort() {
        println(listOf(1, 2, 3, 4, 5).sortedWithOrderList(iterable = listOf(1, -1, 4, 2, 3)).mkString())
    }

    @Test
    fun sort1() {
        val orderList = listOf(4, 2, 3, 1)
        val m = arrayOf(
            arrayOf(0.0, 200.0, 100.0, 100.0),
            arrayOf(150.0, 0.0, 250.0, 400.0),
            arrayOf(0.0, 0.0, 0.0, 0.0),
            arrayOf(0.0, 0.0, 0.0, 0.0)
        ).toList().sortedWithOrderList(orderList).map {
            it.toList().sortedWithOrderList(orderList).toTypedArray()
        }
        m.forEach {
            println(it.toList().mkString())
        }
    }

    @Test
    fun matrix() {
        Matrix(
            arrayOf(
                doubleArrayOf(-400.0, 150.0, 0.0, 0.0),
                doubleArrayOf(200.0, -800.0, 0.0, 0.0),
                doubleArrayOf(100.0, 250.0, -1.0, 0.0),
                doubleArrayOf(100.0, 400.0, 0.0, -1.0)
            )
        ).solve(
            Matrix(
                arrayOf(
                    doubleArrayOf(-60000.0),
                    doubleArrayOf(-48000.0),
                    doubleArrayOf(-70000.0),
                    doubleArrayOf(-62000.0)
                )
            )
        ).show()
    }
}