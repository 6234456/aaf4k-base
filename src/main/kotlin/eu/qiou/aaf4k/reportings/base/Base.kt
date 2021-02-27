package eu.qiou.aaf4k.reportings.base

/**
 * @param structure the account mapping of accounts to its sub-accounts
 */

class Base(val structure: List<Pair<Int, Int>>, val accounts: List<Base.Account>) {
    data class Account(
        val id: Int, val name: String = "", val accountId: Int = id, val accountIdPrefix: String = "",
        val accountIdAffix: String = "", val value: Double = 0.0, val type: ReportingType = ReportingType.ASSET
    )

    data class Entry(val id: Int, val desc: String, val accounts: List<Account>)

    data class Category(val id: Int, val name: String, val desc: String, val entries: List<Entry>)

    fun update(category: Base.Category): Base {
        return this
    }

    fun restruct(): Map<Int, List<Int>> {
        return structure.groupBy { it.first }.mapValues { it.value.map { x -> x.second } }
    }


}