package eu.qiou.aaf4k.schemata

import org.junit.Test

import org.junit.Assert.*

class ExpressionTest {

    @Test
    fun value() {
        val v = Constant("Tarif", 0.05) * Variable("trail", 23.2) + Variable("§ 32d Abs.2 Satz 3 EstG", 32.0)

        print(v.value)
    }
}