package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.foldTrackListInit
import eu.qiou.aaf4k.util.mapValuesIndexed
import eu.qiou.aaf4k.util.mergeReduce
import eu.qiou.aaf4k.util.time.TimeSpan

interface Cashflow {
    fun cashflow(): Map<TimeSpan, Double>

    fun forPeriod(period: TimeSpan): Double = this.cashflow().getOrDefault(period, 0.0)

    fun forPeriod(year: Int): Double = this.cashflow().getOrDefault(TimeSpan.forYear(year), 0.0)

    fun corrent(depositInterest: Double, financingInterest: Double, taxRate: Double): Cashflow {
        val cashflow = this.cashflow()
        val value = cashflow.values

        val res = value.foldTrackListInit(value.first()) { acc, x, i ->
            if (i == 0) {
                acc * -1
            } else {
                acc + acc * (if (acc < 0) depositInterest else financingInterest) * (1 - taxRate) - x
            }
        }

        return object : Cashflow {
            override fun cashflow(): Map<TimeSpan, Double> {
                return cashflow.mapValuesIndexed { _, i -> res[i] }
            }
        }
    }


    operator fun plus(other: Cashflow): Cashflow = object : Cashflow {
        override fun cashflow(): Map<TimeSpan, Double> {
            return this@Cashflow.cashflow().mergeReduce(other = other.cashflow()) { x, y -> x + y }
        }
    }

    companion object {
        fun consolidate(items: Iterable<Cashflow>): Cashflow = items.reduce { acc, cashflow -> acc + cashflow }
    }
}