package eu.qiou.aaf4k.util.time

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class TimeSpanTest {

    val span = TimeSpan.forYear(2019)

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
        }.getParents()!!.first(), TimeSpan(LocalDate.of(2019, 6, 10), LocalDate.of(2019, 6, 16))
        )

        assertEquals(TimeSpan(
            LocalDate.of(2019, 6, 10),
            LocalDate.of(2019, 6, 15)
        ).apply {
            rollUpTo = setOf(TimeSpan.ChronoSpan(1, ChronoUnit.MONTHS))
        }.getParents()!!.first(), TimeSpan(LocalDate.of(2019, 6, 1), LocalDate.of(2019, 6, 30))
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

    }

    @Test
    fun contains1() {
    }

    @Test
    fun plus() {
    }

    @Test
    fun plus1() {
    }

    @Test
    fun minus() {
    }

    @Test
    fun rollForward() {
    }

    @Test
    fun drillDown() {
    }

    @Test
    fun rollUp() {
    }

    @Test
    fun isInOneDay() {
    }

    @Test
    fun isInOneYear() {
    }

    @Test
    fun isInOneWeek() {
    }

    @Test
    fun isInOneHalfYear() {
    }

    @Test
    fun isInOneMonth() {
    }

    @Test
    fun isInOneQuarter() {
    }

    @Test
    fun getContainingYear() {
    }

    @Test
    fun getContainingHalfYear() {
    }

    @Test
    fun getContainingMonth() {
    }

    @Test
    fun getContainingWeek() {
    }

    @Test
    fun getContainingQuarter() {
    }

    @Test
    fun toString1() {
    }

    @Test
    fun getStart() {
    }

    @Test
    fun getEnd() {
    }

    @Test
    operator fun component1() {
    }

    @Test
    operator fun component2() {
    }

    @Test
    fun copy() {
    }

    @Test
    fun hashCode1() {
    }

    @Test
    fun equals1() {
    }
}