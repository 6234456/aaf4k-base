package eu.qiou.aaf4k.schemata

import org.junit.Test

import java.time.LocalDate

class CitationTest {

    @Test
    fun plus() {
        val citation =
            Citations.PARAGRAPH * "3" + Citations.SECTION * "40" + Citations.SENTENCE * "1" + Citations.LIT * "d"
        val citation1 =
            Citations.PARAGRAPH * "3" + Citations.SECTION * "40" + Citations.SENTENCE * "1" + Citations.LIT * "d"

        println(citation)
        println(Source(Legislation.DE_ESTG, citation))
        println(Source(Legislation.DE_BMF_SCHREIBEN, Citations.NOTE * 12, LocalDate.now()))
        println(citation == citation1)

    }
}