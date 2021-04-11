package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.*
import eu.qiou.aaf4k.util.time.TimeSpan

interface Cashflow {
    fun cashflow(): Map<TimeSpan, Double>

    fun forPeriod(period: TimeSpan): Double = this.cashflow().getOrDefault(period, 0.0)

    fun forPeriod(year: Int): Double = this.cashflow().getOrDefault(TimeSpan.forYear(year), 0.0)

    fun corrent(depositInterest: Double, financingInterest: Double, taxRate: Double): Cashflow {
        val cashflow = this.cashflow()
        val value = cashflow.values

        val res = value.foldTrackList(value.first()) { acc, x, i ->
            if (i == 0) {
                acc * -1
            } else {
                acc + acc * (if (acc < 0) depositInterest else financingInterest) * (1 - taxRate) - x
            }
        }

        return toCashflow(cashflow.mapValuesIndexed { _, i -> res[i] })
    }

    fun str(decimalPrecision: Int = 4): String = cashflow().values.map { it.roundUpTo(decimalPrecision) }.mkString()


    operator fun plus(other: Cashflow): Cashflow =
        toCashflow(this@Cashflow.cashflow().mergeReduce(other = other.cashflow()) { x, y -> x + y })

    operator fun plus(other: Iterable<Double>): Cashflow = toCashflow(other, this.cashflow().keys.sortedBy { it })

    companion object {
        fun toCashflow(items: Iterable<Double>, periods: Iterable<TimeSpan>): Cashflow =
            // fill the rest periods with 0
            toCashflow(periods.zip(items + (if (periods.count() > items.count()) (1..periods.count() - items.count()).map { 0.0 } else listOf())).toMap())

        fun toCashflow(map: Map<TimeSpan, Double>): Cashflow = object : Cashflow {
            override fun cashflow(): Map<TimeSpan, Double> {
                return map
            }
        }
        fun consolidate(items: Iterable<Cashflow>): Cashflow = items.reduce { acc, cashflow -> acc + cashflow }
    }
}