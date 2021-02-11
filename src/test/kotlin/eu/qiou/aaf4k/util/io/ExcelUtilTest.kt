package eu.qiou.aaf4k.util.io

import org.junit.Test

class ExcelUtilTest {
    val sht = ExcelUtil.getWorksheet("DE_SuSa.xlsx", sheetName = "SuSa").first

    @Test
    fun x() {
        println(ExcelUtil.x(sht))
        println(ExcelUtil.y(sht, 4))
    }

    @Test
    fun y() {
    }

    @Test
    fun getWorksheet() {
    }
}