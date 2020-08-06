package eu.qiou.aaf4k.schemata

import java.util.*

enum class Citations(var number: String = "") {
    PARAGRAPH,
    SECTION, // Absatz
    SENTENCE,
    NUMBER,
    HALF_SENTENCE,
    NOTE, // Textziffer
    LETTER,
    DOUBLE_LETTER;

    override fun toString(): String {
        return when (this) {
            PARAGRAPH -> "§ $number"
            SECTION -> "Abs. $number"
            SENTENCE -> "Satz $number"
            NUMBER -> "Nr. $number"
            NOTE -> "Tz. $number"
            HALF_SENTENCE -> "${number}. Halbsatz"
            DOUBLE_LETTER -> "Doppelbuchstabe $number"
            LETTER -> "Buchstabe $number"
        }
    }
}

operator fun Citations.times(number: String): Citation {
    return Citation() + this.apply {
        this.number = number
    }
}

operator fun Citations.times(number: Int): Citation = this * number.toString()

