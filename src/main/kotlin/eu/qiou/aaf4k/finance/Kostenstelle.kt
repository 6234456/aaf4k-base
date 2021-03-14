package eu.qiou.aaf4k.finance

import eu.qiou.aaf4k.reportings.base.Identifiable


data class Kostenstelle(override val id: Long, val name: String) : Identifiable {
    companion object {
        val GENERAL_COST = Kostenstelle(0, "General Cost")
    }
}