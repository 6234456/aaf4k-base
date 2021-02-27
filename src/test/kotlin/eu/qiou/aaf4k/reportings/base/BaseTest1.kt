package eu.qiou.aaf4k.reportings.base

import org.junit.Test

class BaseTest {

    @Test
    fun update() {
        val b = Base(
            listOf(
                1 to 2,
                1 to 3,
                1 to 4,
                2 to 21,
                2 to 22,
                22 to 231
            ),
            listOf(
                Base.Account(1, "Total"),
                Base.Account(2, "2"),
                Base.Account(3, "2 2"),
                Base.Account(4, "3 2"),
                Base.Account(21, "2 21"),
                Base.Account(22, "2 22"),
                Base.Account(231, "2 22 231")
            )
        )

        println(b.restruct())
    }
}