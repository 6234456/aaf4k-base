package eu.qiou.aaf4k.util.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.test.assertFails


class TimeSpanTest {

    private val span = TimeSpan.forYear(2019)

    @Test
    fun getDrillDownTo() {
        assertEquals(span.drillDownTo.amount, 1)
        assertEquals(span.drillDownTo.unit, ChronoUnit.MONTHS)
    }

    @Test
    fun setDrillDownTo() {
        span.drillDownTo = TimeSpan.ChronoSpan(4)
    }

    @Test
    fun getRollUpTo() {
    }

    @Test
    fun setRollUpTo() {
        assertEquals(TimeSpan(
            LocalDate.of(2019, 6, 10),
            LocalDate.of(2019, 6, 15)
        ).apply {
            rollUpTo = setOf(TimeSpan.ChronoSpan(1, ChronoUnit.WEEKS))
        }.getParents()!!.first(),
            TimeSpan(LocalDate.of(2019, 6, 10), LocalDate.of(2019, 6, 16))
        )

        assertEquals(TimeSpan(
            LocalDate.of(2019, 6, 10),
            LocalDate.of(2019, 6, 15)
        ).apply {
            rollUpTo = setOf(TimeSpan.ChronoSpan(1, ChronoUnit.MONTHS))
        }.getParents()!!.first(),
            TimeSpan(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 6, 30))
        )
    }

    @Test
    fun getChildren() {
        span.drillDownTo = TimeSpan.ChronoSpan(4)
        assertEquals(
            span.getChildren().first(), TimeSpan(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 4, 30)
            )
        )
    }

    @Test
    fun getParents() {
    }

    @Test
    fun contains() {
        assertEquals(span.contains(TimeSpan.forMonth(2019, 10) + ChronoUnit.DAYS * 12), true)
        assertEquals(span.contains(TimeSpan.forMonth(2019, 12) + ChronoUnit.DAYS * 1), false)
        assertEquals(span.contains(TimeSpan.forDay(2019, 12, 31) + ChronoUnit.DAYS * 1), false)
    }

    @Test
    fun contains1() {
        assertEquals(TimeSpan.forQuarter(2019, 4) in span, true)
    }

    @Test
    fun plus() {
        assertEquals(
            LocalDate.of(2019, 6, 16),
            LocalDate.of(2019, 6, 15).endOfWeek()
        )
        assertEquals(
            LocalDate.of(2019, 6, 16),
            LocalDate.of(2019, 6, 14).endOfWeek()
        )
        assertEquals(
            LocalDate.of(2019, 6, 16),
            LocalDate.of(2019, 6, 13).endOfWeek()
        )
        assertEquals(
            LocalDate.of(2019, 6, 16),
            LocalDate.of(2019, 6, 12).endOfWeek()
        )
        assertEquals(
            LocalDate.of(2019, 6, 16),
            LocalDate.of(2019, 6, 11).endOfWeek()
        )
        assertEquals(
            LocalDate.of(2019, 6, 16),
            LocalDate.of(2019, 6, 10).endOfWeek()
        )
        assertEquals(
            LocalDate.of(2019, 1, 6),
            LocalDate.of(2018, 12, 31).endOfWeek()
        )

        assertEquals(
            LocalDate.of(2018, 12, 31),
            LocalDate.of(2019, 1, 1).startOfWeek()
        )
        assertEquals(
            LocalDate.of(2018, 12, 31),
            LocalDate.of(2019, 1, 6).startOfWeek()
        )
    }

    @Test
    fun plus1() {
    }

    @Test
    fun minus() {
        assertEquals(true, LocalDate.of(1998, 12, 31) in TimeSpan.forWeek(1998, 53))
        assertFails { TimeSpan.forWeek(1998, 54) }
    }

    @Test
    fun rollForward() {
    }

    @Test
    fun drillDown() {
        assertEquals("[1997-12-29, 1998-01-04]",
            TimeSpan.forYear(1998).apply { drillDownTo = TimeSpan.ChronoSpan(unit = ChronoUnit.WEEKS) }
                .drillDown().first().toString())
    }

    @Test
    fun rollUp() {
        assertEquals(false, LocalDate.of(1999, 1, 1) in TimeSpan.forWeek(1999, 1))
    }

    @Test
    fun isInOneDay() {
        assertEquals(true, TimeSpan.forDay(2019, 12, 31).isInOneDay())
    }

    @Test
    fun isInOneYear() {
        assertEquals(false, TimeSpan.forWeek(1998, 53).isInOneYear())
    }

    @Test
    fun isInOneWeek() {
        assertEquals(true, TimeSpan.forWeek(1998, 53).isInOneWeek())
    }


    @Test
    fun getContainingHalfYear() {
        assertFails {
            TimeSpan.forWeek(1998, 53).getContainingQuarter()
        }

        assertFails {
            TimeSpan.forWeek(1998, 53).getContainingHalfYear()
        }

        assertEquals(
            TimeSpan(
                LocalDate.of(2019, 1, 1),
                LocalDate.of(2019, 6, 30)
            ),
            TimeSpan.forMonth(2019, 6).getContainingHalfYear()
        )

        assertEquals(
            TimeSpan(
                LocalDate.of(2019, 7, 1),
                LocalDate.of(2019, 12, 31)
            ),
            TimeSpan.forMonth(2019, 7).getContainingHalfYear()
        )
    }

    @Test
    fun getContainingMonth() {
        assertEquals(
            LocalDate.of(2019, 3, 31),
            LocalDate.of(2019, 2, 28).ofNext(2, withIntervalUnit = ChronoUnit.MONTHS).last()
        )

        assertEquals(
            LocalDate.of(2019, 2, 28),
            LocalDate.of(2019, 1, 30).ofNext(2, withIntervalUnit = ChronoUnit.MONTHS).last()
        )

        assertEquals(true, TimeSpan.forDay(1999, 1, 1).isInOneWeek())
        assertEquals(true, TimeSpan.forDay(1999, 1, 1).isInOneMonth())
        assertEquals(true, TimeSpan.forMonth(1999, 3).isInOneQuarter())
        assertEquals(true, TimeSpan.forMonth(1999, 3).isInOneHalfYear())
    }

    @Test
    fun getContainingWeek() {
        assertEquals(TimeSpan.forWeek(1998, 53), TimeSpan.forDay(1999, 1, 1).getContainingWeek())
        assertEquals(true, ChronoUnit.WEEKS.toPercentageOfYear() == 1.0 / 52)
    }

    @Test
    fun getContainingQuarter() {
        assertFails { TimeSpan.forWeek(1998, 1).getContainingQuarter() }
        assertEquals(TimeSpan.forHalfYear(2019, true).getContainingYear(), TimeSpan.forYear(2019))
        assertEquals(TimeSpan.forWeek(2019, 2).getContainingMonth(), TimeSpan.forMonth(2019, 1))
        assertEquals(TimeSpan.forWeek(2019, 1),
            TimeSpan.forDay(2019, 1, 1).apply {
                rollUpTo = setOf(TimeSpan.ChronoSpan(1, ChronoUnit.WEEKS))
            }.rollUp().first()
        )
    }

    @Test
    fun toString1() {
        assertEquals(
            LocalDate.of(2019, 2, 28),
            LocalDate.of(2019, 2, 3).endOfMonth()
        )
    }

    @Test
    fun getStart() {
        assertEquals(LocalDate.of(2019, 1, 1), span.start)
    }

    @Test
    fun getEnd() {
        assertEquals(LocalDate.of(2019, 12, 31), span.end)
    }

}