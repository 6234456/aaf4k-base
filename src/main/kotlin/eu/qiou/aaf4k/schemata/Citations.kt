package eu.qiou.aaf4k.schemata

import java.util.*

enum class Citations(var number: String = "") {
    PARAGRAPH,
    ARTICLE,
    BOOK,
    BAND,
    CHAPTER,
    SECTION, // Absatz
    SENTENCE,
    NUMBER,
    HALF_SENTENCE,
    ALTERNATIVE,
    NOTE, // Textziffer
    LETTER,
    DOUBLE_LETTER;

    override fun toString(): String {
        return when (this) {
            PARAGRAPH -> "§ $number"
            ARTICLE -> "Art. $number"
            BOOK -> "Book $number"
            BAND -> "Band $number"
            CHAPTER -> "Ch. $number"
            SECTION -> "Abs. $number"
            SENTENCE -> "Satz $number"
            NUMBER -> "Nr. $number"
            NOTE -> "Tz. $number"
            HALF_SENTENCE -> "${number}. Halbsatz"
            ALTERNATIVE -> "Alternativ ${number}"
            DOUBLE_LETTER -> "Doppelbuchstabe $number"
            LETTER -> "Buchstabe $number"
        }
    }

    companion object {
        private val symbols = listOf(
            "$",
            "Art.",
            "Book",
            "Band",
            "Ch.",
            "Abs.",
            "Satz",
            "Nr.",
            "Tz.",
            ". Halbsatz",
            "Alternativ",
            "Doppelbuchstabe",
            "Buchstabe"
        )
        private val symbolsCitation = listOf(
            PARAGRAPH,
            ARTICLE,
            BOOK,
            BAND,
            CHAPTER,
            SECTION,
            SENTENCE,
            NUMBER,
            NOTE,
            HALF_SENTENCE,
            ALTERNATIVE,
            DOUBLE_LETTER,
            LETTER
        )

        fun of(src: String) {
            // "§ 32a 3 Art. 3 Nr. 5 EStG"

            val s = mutableListOf<String>()
            val sym = mutableListOf<Citations>()
            val nr = mutableListOf<String>()
            var start = 0
            for (i in 0..src.length) {
                val temp = src.substring(start, i)
                if (temp in symbols || i == src.length - 1) {
                    if (s.isNotEmpty()) {
                        nr.add(s.joinToString(" "))
                        s.removeAll { true }
                    }

                    if (i != src.length - 1)
                        sym.add(symbolsCitation[symbols.indexOf(temp)])

                    start = i + 1
                } else if (i < src.length - 1 && src.substring(i, i + 1) == " ") {
                    temp.let { if (it.isNotEmpty()) s.add(it) }
                    start = i + 1
                }
            }

            println(sym.zip(nr))
        }
    }
}

operator fun Citations.times(number: String): Citation {
    return Citation() + this.apply {
        this.number = number
    }
}

operator fun Citations.times(number: Int): Citation = this * number.toString()

