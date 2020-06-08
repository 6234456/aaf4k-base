package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.reportings.GlobalConfiguration
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import eu.qiou.aaf4k.util.unit.ProtoUnit
import java.time.LocalDate

/**
 * @param id:                       the account id, identifier
 * @param decimalPrecision:         precision of the underlying data, corresponding to the decimalPosition of decimalValue
 * @param unit                      unit of the underlying data
 * @param desc                      description of the account
 * @param timeParameters            specify the time point or time range attribute of the account
 * @param entity                    the information about the reporting entity
 * @param isStatistical             the value will not be aggregate to the parent if set true
 * @param validateUntil             compare the reporting date and validateUntil to throw warnings
 * @param reportingType             related to the categorization of the financial accounting
 * @param displayUnit               the value to display, corresponding to the displayValue
 */

data class CollectionAccount(override val id: Long, override val name: String,
                             override val decimalPrecision: Int = GlobalConfiguration.DEFAULT_DECIMAL_PRECISION,
                             override val unit: ProtoUnit = CurrencyUnit(decimalPrecision = decimalPrecision),
                             override val desc: String = "",
                             override val timeParameters: TimeParameters? = null,
                             override val entity: Entity? = null,
                             override val isStatistical: Boolean = false,
                             override val validateUntil: LocalDate? = null,
                             override val reportingType: ReportingType = ReportingType.AUTO,
                             override val displayUnit: ProtoUnit = unit
) : ProtoCollectionAccount {

    override val subAccounts: MutableList<ProtoAccount> = mutableListOf()
    override val superAccounts: MutableList<ProtoCollectionAccount> = mutableListOf()

    override fun deepCopy(): ProtoAccount {
        return copy(id = id).apply {
            subAccounts.clear()
            superAccounts.clear()
            this@CollectionAccount.subAccounts.forEach {
                this + it.deepCopy()
            }
        }
    }

    fun copyWith(reportingType: ReportingType): CollectionAccount {
        return copy(reportingType = reportingType).apply {
            subAccounts.clear()
            superAccounts.clear()
            this@CollectionAccount.subAccounts.forEach {
                this + when (it) {
                    is CollectionAccount -> it.copyWith(reportingType)
                    is Account -> it.copy(reportingType = reportingType)
                    else -> throw Exception("unknown type")
                }
            }
        }
    }

    override var toUpdate = false

    override var cacheList: List<ProtoAccount> = listOf()
    override var cacheAllList: List<ProtoAccount> = listOf()

    override fun toString(): String = toStrings()
}