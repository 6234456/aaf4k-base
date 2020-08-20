package eu.qiou.aaf4k.schemata

import java.lang.Exception
import java.time.LocalDate
import java.util.*

data class Source(
    val legislation: Legislation, val citation: Citation,
    val validate: LocalDate? = null
) {

    constructor(legislation: String, citation: String, validate: LocalDate? = null) : this(
        Legislation.of(legislation),
        Citations.of(citation),
        validate
    )

    override fun toString(): String {
        return when (legislation) {
            Legislation.DE_BMF_SCHREIBEN -> "$citation ${legislation.abbr} vom $validate"
            Legislation.DE_ESTH -> "H $citation ${legislation.abbr}"
            Legislation.DE_ESTR -> "R $citation ${legislation.abbr}"
            else -> "$citation ${legislation.abbr}${if (validate != null) " vom $validate" else ""}"
        }
    }
}

enum class Legislation(val locale: Locale, val desc: String, val abbr: String) {
    DE_HGB(Locale.GERMAN, "Handelsgesetzbuch", "HGB"),
    DE_BGB(Locale.GERMAN, "Bürgergesetzbuch", "BGB"),
    DE_BMF_SCHREIBEN(Locale.GERMAN, "BMF Schreiben", "BMF Schreiben"),
    DE_ESTG(Locale.GERMAN, "Einkommensteuergesetz", "EStG"),
    DE_ESTR(Locale.GERMAN, "Einkommensteuerrichtlinie", "EStR"),
    DE_ESTH(Locale.GERMAN, "Einkommensteuerrichthinweis", "EStH");

    companion object {
        fun of(abbr: String): Legislation {
            return Legislation.values().firstOrNull { it.abbr == abbr } ?: throw Exception("Unknown Abbr: $abbr")
        }
    }
}


