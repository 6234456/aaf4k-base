package eu.qiou.aaf4k.util.template

import org.junit.Test

class TemplateTest {

    @Test
    fun build() {
        Template(
            theme = Template.Theme.BLACK_WHITE, data = listOf(
                mapOf("trail" to 1, "trail1" to 2),
                mapOf("trail" to 11, "trail1" to 2.23),
                mapOf("trail" to 121, "trail1" to 12)
            ), headings = listOf(Template.HeadingFormat("trail"), Template.HeadingFormat("trail1"))
        )
            .build("data.xls", sheetName = "1213", top = 10, left = 3)
    }
}