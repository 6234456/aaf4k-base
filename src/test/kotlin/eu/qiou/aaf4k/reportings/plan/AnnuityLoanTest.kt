package eu.qiou.aaf4k.reportings.plan

import eu.qiou.aaf4k.util.time.TimeSpan
import org.junit.Test
import java.lang.Math.pow
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class AnnuityLoanTest {

    @Test
    fun cashflow() {
        val c = AnnuityLoan(1000.0,
            TimeSpan(LocalDate.of(2020, 1, 1), LocalDate.of(2025, 12, 31)).drillDown(2, ChronoUnit.YEARS),
            0.15)

        println(c)
        println(c.corrent(0.15, 0.0, 0.30).str())
        println(c.effectiveInterestRate())
        println(PresentValue((1..3).map { 437.9769618430527 }, 0.15).value)
    }

    @Test
    fun vofi() {
        val periods = listOf(TimeSpan.forDay(2020, 1, 1)) + TimeSpan.forYear(2020, 2024).drillDown()
        val investment: Cashflow =
            Cashflow.toCashflow(listOf(-760_000.0, 240_000.0, 320_000.0, 180_000.0, 120_000.0, 160_000.0), periods)
        val equity: Cashflow = Cashflow.toCashflow(listOf(760_000.0 * 0.2), periods)
        val annuityLoan = AnnuityLoan(760_000.0 * 0.3, TimeSpan.forYear(2020, 2024).drillDown(), 0.08)
        val endMaturityLoan = EndMaturityLoan(
            760_000.0 * 0.3 / 0.95, TimeSpan.forYear(2020, 2024).drillDown(),
            0.05, 0.07
        )

        listOf(investment, equity, annuityLoan, endMaturityLoan).forEach {

            println("${it.cashflow().size} " + it.str())
        }

        println(
            Cashflow.consolidate(listOf(investment, equity, annuityLoan, endMaturityLoan))
                .corrent(0.05, 0.10, 0.0).str()
        )
    }

    @Test
    fun vofi2() {
        val endYear = 2028
        val periods = listOf(TimeSpan.forDay(2021, 1, 1)) + TimeSpan.forYear(2021, endYear).drillDown()
        val investment: Cashflow =
            Cashflow.toCashflow(listOf(-125_000.0, 32_000.0, 28_000.0, 40_000.0, 45_000.0), periods)
        val equity: Cashflow = Cashflow.toCashflow(listOf(60_000.0), periods)
        val endMaturityLoan = EndMaturityLoan(
            28_000.0, TimeSpan.forYear(2021, endYear).drillDown(),
            0.10, 0.06
        )

        listOf(investment, equity, endMaturityLoan).forEach {
            println("${it.cashflow().size} " + it.str())
        }

        println(
            Cashflow.consolidate(listOf(investment, equity, endMaturityLoan))
                .corrent(0.07, 0.12, 0.0).str()
        )
        println(60_000.0 * pow(1.07, 4.0))
    }
}