package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.util.filterMapInPlace

/**
 *  top-down
 */
interface ProtoCollectionAccount : ProtoAccount, Drilldownable<ProtoCollectionAccount, ProtoAccount> {
    val subAccounts: MutableList<ProtoAccount>

    override var value: Long
        get() = if (subAccounts.isEmpty()) 0L else subAccounts.map { if (it.isStatistical) 0 else it.value }
            .reduce { acc, l -> acc + l }
        set(value) {
            throw Exception("set $value to the CollectionAccount $name is not forbidden.")
        }

    override fun getChildren(): Collection<ProtoAccount> = subAccounts

    override fun getParents(): Collection<ProtoCollectionAccount>? = superAccounts

    override fun add(child: ProtoAccount, index: Int?, percentage: Double): ProtoCollectionAccount {
        //do nothing if child already exists and this account is not statistical account
        if (!isStatistical && child in this)
            return this

        if (index == null)
            subAccounts.add(child)
        else
            subAccounts.add(index, child)

        child.superAccounts.add(this)
        return super.add(child, index, 1.0)
    }

    override fun remove(child: ProtoAccount): ProtoCollectionAccount {
        if (child in this) {
            subAccounts.remove(child)
            child.superAccounts.remove(this)
            super.remove(child)
        }
        return this
    }

    /**
     *  recursively replace all the target account
     */
    fun replace(target: ProtoAccount, newAccount: ProtoAccount, updateStructure: Boolean = true) {
        target.superAccounts.filterMapInPlace({ _, _ -> true }) { sup ->
            sup.subAccounts.filterMapInPlace({ o, _ -> o.id == target.id }) {
                if (newAccount.reportingType == ReportingType.AUTO) {
                    if (newAccount is Account) {
                        newAccount.copy(reportingType = it.reportingType)
                    } else {
                        newAccount as CollectionAccount
                        newAccount.copyWith(it.reportingType)
                    }
                } else {
                    newAccount
                }.apply {
                    this.superAccounts.add(sup)
                }
            }

            sup
        }

        // structure of the CollectionAccount is changed, notified
        toUpdate = updateStructure
    }

    fun replace(target: Long, newAccount: ProtoAccount, updateStructure: Boolean = true) {
        replace(search(target)!!, newAccount, updateStructure)
    }

    /**
     * enables the two-stage structure
     *
     *
     */
    fun replace(newAccount: ProtoAccount, updateStructure: Boolean = true) {
        replace(search(newAccount.id)!!, newAccount, updateStructure)
    }

    fun removeRecursively(target: Long) {
        removeRecursively(search(target)!!)
    }

    /**
     * map all the accounts in place
     */
    fun map(mutator: (ProtoAccount) -> ProtoAccount): ProtoCollectionAccount {
        subAccounts.filterMapInPlace({ _, _ -> true }) {
            when (it) {
                is Account -> mutator(it)
                is CollectionAccount -> {
                    (mutator(it) as CollectionAccount).addAll(it.map(mutator).subAccounts)
                }
                else -> throw Error("unknown type ${it::class.qualifiedName}")
            }
        }

        this.toUpdate = true

        return this
    }

    override fun copyWith(value: Double, decimalPrecision: Int): ProtoAccount {
        throw Exception("Applicable only to the atomic accounts.")
    }

    override fun nullify(): ProtoAccount {
        return (deepCopy() as ProtoCollectionAccount).apply {
            subAccounts.map { it.nullify() }.let {
                subAccounts.clear()
                subAccounts.addAll(it)
            }
        }
    }
}