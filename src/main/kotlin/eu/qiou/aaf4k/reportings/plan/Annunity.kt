package eu.qiou.aaf4k.reportings.plan

import kotlin.math.pow

class Annunity(numberOfPeriod: Int, interestRate: Double) {
    val rate = (1.0 - (1.0 + interestRate).pow(numberOfPeriod.toDouble() * -1)) / interestRate

    fun presentValue(payment: Double): Double {
        return payment * rate
    }

    fun payment(presentValue: Double): Double {
        return presentValue / rate
    }
}