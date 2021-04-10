package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.mkString
import eu.qiou.aaf4k.util.time.TimeSpan

abstract class Loan(val payments: List<Double>, val pal: Double, val period: TimeSpan) : Cashflow {
    fun effectiveInterestRate(): Double = TrailAndError(function = { PresentValue(payments, it).value + pal }).target()

    override fun toString(): String {
        return cashflow().values.mkString()
    }
}