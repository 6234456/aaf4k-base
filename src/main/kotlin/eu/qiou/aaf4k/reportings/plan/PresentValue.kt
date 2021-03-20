package eu.qiou.aaf4k.reportings.plan

import kotlin.math.pow

class PresentValue(
    cashflowPhase1: List<Double>,
    private val r: Double,
    cashflowPhase2: Double? = null,
    growthRate: Double = 0.0
) {
    private val n = cashflowPhase1.size

    val discountRate: List<Double> = (1..n).map {
        1.0 / (1 + r).pow(it)
    } + if (cashflowPhase2 == null) listOf() else listOf(1.0 / (r - growthRate) / (1 + r).pow(n))

    val presentValue: List<Double> =
        (cashflowPhase1 + if (cashflowPhase2 == null) listOf() else listOf(cashflowPhase2)).zip(discountRate).map {
            it.first * it.second
        }

    val value = presentValue.reduce { acc, d -> acc + d }
}