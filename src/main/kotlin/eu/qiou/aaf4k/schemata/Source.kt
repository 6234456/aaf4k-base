package eu.qiou.aaf4k.schemata

import java.time.LocalDate
import java.util.*

data class Source(
    val legislation: Legislation, val citation: Citation,
    val validate: LocalDate? = null
) {
    init {

    }

    override fun toString(): String {
        val temp = when (legislation) {
            Legislation.DE_BMF_SCHREIBEN -> "${legislation.desc} vom $validate"
            else -> "${legislation.desc}"
        }

        return "$citation $temp"
    }
}

enum class Legislation(val locale: Locale, val desc: String) {
    DE_HGB(Locale.GERMAN, "Handelsgesetzbuch"),
    DE_BGB(Locale.GERMAN, "Bürgergesetzbuch"),
    DE_BMF_SCHREIBEN(Locale.GERMAN, "BMF Schreiben"),
    DE_ESTG(Locale.GERMAN, "Einkommensteuergesetz"),
    DE_ESTR(Locale.GERMAN, "Einkommensteuerrichtlinie"),
    DE_ESTH(Locale.GERMAN, "Einkommensteuerrichthinweis"),
}


