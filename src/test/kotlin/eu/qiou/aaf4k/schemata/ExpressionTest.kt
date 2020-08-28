package eu.qiou.aaf4k.schemata

import org.junit.Test

class ExpressionTest {

    @Test
    fun value() {
        val v1 =  Constant(
            10,
            "Teileinkünftsverfahren (40% Steuerfrei)",
            0.4,
            Source("EStG", "§ 3 Nr. 40 Satz 1 Buchstabe d")
        )

        val v2 = Variable(21, "Einkommen aus Dividenden", 1200.2)

        val v3 = Variable(22, "Zinsaufwand i.Z.m. der Dividenden", 200.0)

        val v4 = Constant(
            12, "Steuerabzug zu 60%", 0.6,
            Source("EStG", "§ 3c Abs. 2")
        )

        val v = (v2 - (v1 * v2).toVariable(31, desc = v1.desc)).apply { indentLevel = 1 } - (v3*v4).apply { indentLevel = 1 }

        println(v.update(mapOf(21 to 100.0, 22 to 300.0)))

        v.apply { indentLevel = 1 }.toXl("data.xlsx")

    }
}