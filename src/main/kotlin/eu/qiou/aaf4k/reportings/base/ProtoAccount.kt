package eu.qiou.aaf4k.reportings.base

import eu.qiou.aaf4k.reportings.GlobalConfiguration
import eu.qiou.aaf4k.util.io.JSONable
import eu.qiou.aaf4k.util.roundUpTo
import eu.qiou.aaf4k.util.strings.CollectionToString
import eu.qiou.aaf4k.util.time.TimeParameters
import eu.qiou.aaf4k.util.unit.CurrencyUnit
import eu.qiou.aaf4k.util.unit.ProtoUnit
import java.time.LocalDate

interface ProtoAccount : JSONable, Identifiable {
    override val id: Long
    val name: String
    val decimalPrecision: Int
    var value: Long
    val desc: String
    val unit: ProtoUnit
    val superAccounts: MutableList<ProtoCollectionAccount>
    val timeParameters: TimeParameters?
    val entity: Entity?
    val isStatistical: Boolean
    val validateUntil: LocalDate?

    val isValidate: Boolean
        get() = validateUntil == null || (timeParameters == null) || (timeParameters as TimeParameters).end <= validateUntil

    val reportingType: ReportingType
        get() = ReportingType.AUTO

    val reportingValue: Double
        get() = when (reportingType) {
            ReportingType.AUTO -> Math.abs(displayValue)
            else -> displayValue * reportingType.sign
        }

    val reportingSide: ReportingSide
        get() = when (reportingType) {
            ReportingType.AUTO -> if (displayValue.equals(0.0) or (reportingValue / displayValue > 0)) ReportingSide.DEBTOR else ReportingSide.CREDITOR
            else -> if (reportingType.sign == 1) ReportingSide.DEBTOR else ReportingSide.CREDITOR
        }

    val isAggregate: Boolean
        get() = this is ProtoCollectionAccount

    /** the value is stored in Long-Format to get rid of the rounding error, 12.34 is by default represented as 1234 with decimal position of 2.
     *
     *  the decimal value refers to the value converted to double but <b>before</b> the adjustment of currency and display unit
     *  the decimal value of the collection account is the sum of its children with exception of the statistical account
     */
    val decimalValue: Double
        get() {
            return if (this is ProtoCollectionAccount)
                subAccounts.fold(0.0) { acc, e ->
                    acc + if (e.isStatistical) 0.0 else e.decimalValue
                }
            else {
                val tmp = value.toDouble() / Math.pow(10.0, decimalPrecision.toDouble())
                when (unit) {
                    is CurrencyUnit -> (unit as CurrencyUnit).convertFxTo(GlobalConfiguration.DEFAULT_CURRENCY_UNIT, timeParameters)(tmp)
                    else -> tmp
                }.roundUpTo(decimalPrecision)
            }
        }

    val displayUnit: ProtoUnit
    val displayValue: Double
        get() = when (unit) {
            is CurrencyUnit -> (unit as CurrencyUnit).convertFxTo(displayUnit, timeParameters)(decimalValue)
            else -> unit.convertTo(displayUnit)(decimalValue)
        }.roundUpTo(decimalPrecision)

    val textValue: String
        get() = displayUnit.format()(displayValue)

    // symbol for the collection account
    private fun superAccountStr(): String {
        return "[$id $name] ${if (reportingType != ReportingType.AUTO) reportingType.code else ""}"
    }

    private fun subAccountStr(): String {
        val tmp = if (reportingType != ReportingType.AUTO) reportingType.code else ""
        return if (isStatistical)
            "{$id $name} $tmp : $textValue"
        else
            "($id $name) $tmp : $textValue"
    }

    fun deepCopy(): ProtoAccount

    fun copyWith(value: Double, decimalPrecision: Int): ProtoAccount

    fun copyWith(value: Double): ProtoAccount {
        return copyWith(value, decimalPrecision)
    }

    // change in-place
    fun update(data: Map<Long, Double>, updateMethod: (Double, Double) -> Double = { valueNew, valueOld -> valueNew + valueOld }):ProtoAccount {
        if (this is ProtoCollectionAccount) {
            getChildren().forEach { it.update(data, updateMethod) }
        } else {
            data[id]?.let {
                value = Math.round(updateMethod(it, decimalValue) * Math.pow(10.0, decimalPrecision.toDouble()))
            }
        }

        return this
    }

    fun toStrings(lvl: Int = 0): String {
        if (this is ProtoCollectionAccount && getChildren().isNotEmpty()) {
            return CollectionToString.structuredToStr<ProtoCollectionAccount, ProtoAccount>(this, lvl, ProtoAccount::toStrings, ProtoAccount::superAccountStr)
        }

        return subAccountStr()
    }

    override fun toJSON(): String {
        if (this is ProtoCollectionAccount) {
            return """{"id": $id, "name": "$name", "value": $decimalValue, "displayValue": "$textValue", "decimalPrecision": $decimalPrecision, "desc": "$desc", "hasSubAccounts": true, "hasSuperAccounts": ${this.hasParent()}, "isStatistical": $isStatistical, "timeParameters":${if (timeParameters == null) "null" else timeParameters!!.toJSON()}, "validateUntil":${if (validateUntil == null) "null" else "'$validateUntil'"}, "unit": ${unit.toJSON()}, "displayUnit": ${displayUnit.toJSON()}, "reportingType": "${reportingType.code}", "subAccounts": """ +
                    CollectionToString.mkJSON(subAccounts as Iterable<JSONable>, ",\n") + "}"
        }

        return """{"id": $id, "name": "$name", "value": $decimalValue, "displayValue": "$textValue", "decimalPrecision": $decimalPrecision, "desc": "$desc", "hasSubAccounts": false, "hasSuperAccounts": ${this.superAccounts.size > 0}, "isStatistical": $isStatistical, "timeParameters":${if (timeParameters == null) "null" else timeParameters!!.toJSON()}, "validateUntil":${if (validateUntil == null) "null" else "'$validateUntil'"}, "unit": ${unit.toJSON()}, "displayUnit": ${displayUnit.toJSON()}, "reportingType": "${reportingType.code}"}"""
    }

    fun isEmpty(): Boolean {
        if (this is ProtoCollectionAccount) {
            if (!hasChildren()) return true

            return getChildren().all { it.isEmpty() }
        }

        return value == 0L
    }

    fun shorten(whiteList: Iterable<Long>? = null, blackList: Iterable<Long>? = null): ProtoAccount {
        return if (this is ProtoCollectionAccount) {
            // the collection accounts which have no children or all the children with null-value
            (if (whiteList != null) {
                sortedList().filter { !whiteList.contains(it.id) }
            } else {
                blackList?.map { search(it) } ?: sortedAllList().filter { it.isEmpty() }
            }).fold(deepCopy() as ProtoCollectionAccount) { acc, x ->
                if (x != null) acc.removeRecursively(x) else acc
            }
        } else
            deepCopy()
    }

    fun notStatistical(): List<ProtoAccount> {
        if (!isStatistical) {
            if (this is ProtoCollectionAccount) {
                return subAccounts.fold(listOf()) { acc, protoAccount ->
                    acc + protoAccount.notStatistical()
                }
            }

            return listOf(this)
        }

        return listOf()
    }

    fun nullify(): ProtoAccount

}