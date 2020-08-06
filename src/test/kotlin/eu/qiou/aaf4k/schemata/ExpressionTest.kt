package eu.qiou.aaf4k.schemata

import org.junit.Test

import org.junit.Assert.*

class ExpressionTest {

    @Test
    fun value() {
        val v = Constant(10, "Tarif", 0.05) * Variable(21, "trail", 23123123.2)
        println(v.value)

        println((112.88).toInt())

    }
}