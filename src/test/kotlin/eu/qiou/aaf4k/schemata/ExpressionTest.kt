package eu.qiou.aaf4k.schemata

import org.junit.Test

class ExpressionTest {

    @Test
    fun value() {
        val v = Variable(21, "Einkommen aus Dividenden", 1200.2) * Constant(
            10,
            "Teileinkünftsverfahren (40% Steuerfrei)",
            0.4,
            Source("EStG", "§ 3 Nr. 40 Satz 1 Buchstabe d")
        ) - Variable(22, "Zinsaufwand i.Z.m. der Dividenden", 200.0) * Constant(
            12, "Steuerabzug zu 60%", 0.6,
            Source("EStG", "§ 3c Abs. 2")
        )

        println(v.update(mapOf(21 to 100.0, 22 to 300.0)))

    }
}