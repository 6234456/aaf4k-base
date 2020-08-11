package eu.qiou.aaf4k.schemata

import org.junit.Test

import org.junit.Assert.*

class ExpressionTest {

    @Test
    fun value() {
        val v = Constant(
            10,
            "Teileinkünftsverfahren (40% Steuerfrei)",
            0.4,
            Source(Legislation.DE_ESTG, Citations.PARAGRAPH * 3 + Citations.SECTION * 1 + Citations.NUMBER * 40)
        ) * Variable(21, "trail", 123.2)
        println(v.value)

        println((112.88).toInt())

        Citations.of("§ 32a 3 Art. 3 Nr. 5 Satz 2")

    }
}