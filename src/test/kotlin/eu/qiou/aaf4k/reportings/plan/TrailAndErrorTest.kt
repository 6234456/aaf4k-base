package eu.qiou.aaf4k.reportings.plan

import org.junit.Test
import java.lang.Math.sin

class TrailAndErrorTest {

    @Test
    fun target() {


        val t = TrailAndError(function = { PresentValue(listOf(100.0, -10.0, -10.0, -10.0, -120.0), it).value },
            step = 0.000001,
            tolerance = 0.000001)
        println(t.target())

        val t1 = TrailAndError(function = { it * it * it * it - sin(it) * it * it * it - it * it - 300000 },
            step = 0.00000000001,
            tolerance = 0.00000001)
        print(t1.target())
    }
}