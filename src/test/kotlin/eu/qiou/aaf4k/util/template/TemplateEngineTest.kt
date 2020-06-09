package eu.qiou.aaf4k.util.template

import org.junit.Test

class TemplateEngineTest {
    val d = mapOf("YYYY" to 2020, "1005" to 123.23, "91005" to -12342.21233, "MM" to 12, "DD" to 31)
    @Test
    fun compile() {
        println(this.javaClass.classLoader.getResource("data/cn/CAS.xlt").path)

        ExcelReportingTemplate(
            tpl = this.javaClass.classLoader.getResource("data/cn/CAS.xlt").path,
            shtName = "BS", fmt = "%.0f"
        ).export(
            mapOf("YYYY" to 2020, "1005" to 123.23, "91005" to -12342.21233, "MM" to 12, "DD" to 31),
            path = "trail3.xlsx"
        ) //.compile("data/cn/CAS.xlsx")


    }

    @Test
    fun trail() {
        println(TemplateEngine(fmt = "%.0f").compile("ABCD [YYYY]")(d))
    }
}