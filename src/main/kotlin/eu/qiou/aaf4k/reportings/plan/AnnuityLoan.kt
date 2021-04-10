package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.time.TimeSpan

class AnnuityLoan(
    pal: Double, private val periods: List<TimeSpan>,
    private val interestRate: Double, term: Double = Annuity(periods.size, interestRate).payment(pal)
) :
    Loan(pal = pal,
        payments = (1..periods.size).map { term * -1 },
        period = TimeSpan(start = periods.first().start, end = periods.last().end)) {
    override fun cashflow(): Map<TimeSpan, Double> =
        (listOf(TimeSpan(periods.first().start, periods.first().start) to pal) + periods.zip(payments)).toMap()
}