package eu.qiou.aaf4k.util.template

import org.junit.Test

class TemplateEngineTest {
    val d = mapOf(
        "YYYY" to 2020, "entity" to "Demo GmbH",
        "1005" to 123.23, "91005" to -12342.21233, "MM" to 12, "DD" to 31, "isAG" to true,
        "5700" to "'[Wor1]S1'!A1", "5800" to "'[Wor1]S1'!A2"
    )
    @Test
    fun compile() {
        ExcelReportingTemplate(
            tpl = this.javaClass.classLoader.getResource("data/cn/CAS.xlsx").path,
            shtName = "BS", fmt = "%.0f"
        ).export(
            d,
            path = "trail3.xlsx"
        ) //.compile("data/cn/CAS.xlsx")
    }

    @Test
    fun trail() {
        //println(TemplateEngine(fmt = "%s").compile("[= [isAG] ? '所有者权益': '股东权益' | %.0f ]")(d))
        println(
            TemplateEngine(
                fmt = "%s",
                prefix = "{",
                affix = "}"
            ).compile("{= \"{5700}\".concat('+').concat(\"{5800}\")  }")(d)
        )
    }
}