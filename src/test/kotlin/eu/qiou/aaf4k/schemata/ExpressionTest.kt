package eu.qiou.aaf4k.schemata

import eu.qiou.aaf4k.util.io.ExcelUtil
import org.junit.Test

class ExpressionTest {

    @Test
    fun value() {
        val v1 =  Constant(
            10,
            "Teileinkünftsverfahren (40% Steuerfrei)",
            0.4,
            Source("EStG", "§ 3 Nr. 40 Satz 1 Buchstabe d"),
            ExcelUtil.DataFormat.PERCENTAGE, -1
        )

        val v2 = Variable(21, "Einkommen aus Dividenden", 1200.2, indentLevel = 0)

        val v3 = Variable(22, "Zinsaufwand i.Z.m. der Dividenden", 200.0, indentLevel = 0)

        val v4 = Constant(
            12, "Steuerabzug zu 60%", 0.6,
            Source("EStG", "§ 3c Abs. 2"), ExcelUtil.DataFormat.PERCENTAGE, -1
        )

        val v = (v2 - (v1 * v2).apply { indentLevel = 3 }) - (v3 * v4).apply { indentLevel = 3 }

     //   println(v.byId(10))

        println(v.formula())


        (v.update(mapOf(21 to 100.0, 22 to 300.0)) as Expression).toXl("data.xlsx")

    }

    @Test
    fun trail1(){
        // index 2 and 4

        val x = "R2C34".replace("""R(\s*(-?\d+)\s*)?C(\s*(-?\d+)\s*)?""".toRegex()) {
            val r = it.groups[1]!!.value.toInt()
            val c = it.groups[3]?.value?.toInt() ?: 0
            "$r $c"
        }

        println(x)

    }
}