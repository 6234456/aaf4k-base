package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.time.TimeSpan
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AnnuityLoanTest {

    @Test
    fun cashflow() {
        val c = AnnuityLoan(1000.0,
            TimeSpan(LocalDate.of(2020, 1, 1), LocalDate.of(2025, 12, 31)).drillDown(2, ChronoUnit.YEARS),
            0.15)

        println(c)
        println(c.effectiveInterestRate())
        println(PresentValue((1..3).map { 437.9769618430527 }, 0.15).value)
    }
}