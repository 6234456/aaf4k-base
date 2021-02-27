package eu.qiou.aaf4k.schemata

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
    LIT,
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
            HALF_SENTENCE -> "Halbsatz $number"
            ALTERNATIVE -> "Alternativ $number"
            DOUBLE_LETTER -> "Doppelbuchstabe $number"
            LIT -> "Buchstabe $number"
        }
    }

    companion object {
        private val symbols = listOf(
            "§",
            "Art.",
            "Book",
            "Band",
            "Ch.",
            "Abs.",
            "Satz",
            "Nr.",
            "Tz.",
            "Halbsatz",
            "Alternativ",
            "Doppelbuchstabe",
            "Buchstabe"
        ).zip(
            listOf(
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
                LIT
            )
        ).toMap()

        fun of(src: String): Citation {
            // "§ 32a 3 Art. 3 Nr. 5 EStG"

            var tmpString = ""
            var symbol: Citations? = null
            var cnt = 0

            val l = src.split("""\s+""".toRegex())

            return l.fold(Citation()) { acc: Citation, s: String ->
                cnt++

                if (symbols.containsKey(s)) {
                    if (tmpString.isNotEmpty() && symbol != null) {
                        val res = acc + symbol!! * tmpString.trim()
                        symbol = symbols[s]
                        tmpString = ""
                        res
                    } else {
                        symbol = symbols[s]
                        acc
                    }
                } else if (cnt == l.size) {
                    tmpString += " $s"
                    acc + symbol!! * tmpString.trim()
                } else {
                    tmpString += " $s"
                    acc
                }
            }
        }
    }
}

operator fun Citations.times(number: String): Citation {
    return Citation() + this.apply {
        this.number = number
    }
}

operator fun Citations.times(number: Int): Citation = this * number.toString()

