package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.time.TimeSpan

class EndMaturityLoan(
    pal: Double, private val periods: List<TimeSpan>, private val disagio: Double,
    private val norminalInterestRate: Double
) :
    Loan(
        pal = pal,
        payments = (1..periods.size).map { (if (it == periods.size) pal * -1.0 else 0.0) + pal * norminalInterestRate * -1 },
        period = TimeSpan(start = periods.first().start, end = periods.last().end)
    ) {
    override fun cashflow(): Map<TimeSpan, Double> =
        (listOf(
            TimeSpan(
                periods.first().start,
                periods.first().start
            ) to pal * (1 - disagio)
        ) + periods.zip(payments)).toMap()
}