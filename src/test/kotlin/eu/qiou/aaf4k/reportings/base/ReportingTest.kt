package eu.qiou.aaf4k.reportings.base

import org.junit.Test

class ReportingTest {

    val collectionAccount = CollectionAccount(0L, "Demo")
    val reporting = Reporting(collectionAccount)

    @Test
    fun replace() {
        reporting.add(CollectionAccount(1L, "1").apply {
            add(Account(11L, "demo11"))
            add(Account(12L, "demo12", 2L))
        })

        reporting.replace(12L, CollectionAccount(123L, "repl").apply {
            add(Account(23L, "demo1"))
            add(Account(24L, "demo1"))
        })

        reporting.removeRecursively(24L)

        println(reporting)

    }

    @Test
    fun replace1() {
    }

    @Test
    fun search() {
    }

    @Test
    fun shorten() {
    }
}