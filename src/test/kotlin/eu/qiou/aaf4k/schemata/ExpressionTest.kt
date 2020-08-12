package eu.qiou.aaf4k.schemata

import org.junit.Test

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

        println(Citations.of("§ 3 Abs. 1 Nr. 40 Buchstabe d"))
        println((Citations.PARAGRAPH * 3 + Citations.SECTION * 1).contains(Citations.of("§ 3 Abs.     1 Nr.   40 Buchstabe d")))

    }
}