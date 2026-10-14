package com.example.swpp.tododapp.tasks

import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Test

class DueDateUtilsTest {
    @Test
    fun dueDateLabel_activeDueToday_returnsDDay() {
        // GIVEN - An active task due today
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(today, today, isCompleted = false)

        // THEN - The label says D-Day
        assertEquals("D-Day", label)
    }

    @Test
    fun dueDateLabel_activeDueTomorrow_returnsD1() {
        // GIVEN - An active task due one day from today
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(today.plusDays(1), today, isCompleted = false)

        // THEN - The label says D-1
        assertEquals("D-1", label)
    }

    @Test
    fun dueDateLabel_activeDueInThreeDays_returnsD3() {
        // GIVEN - An active task due three days from today
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(today.plusDays(3), today, isCompleted = false)

        // THEN - The label says D-3
        assertEquals("D-3", label)
    }

    @Test
    fun dueDateLabel_activeOverdueByTwoDays_returnsDPlus2() {
        // GIVEN - An active task whose due date was two days ago
        val today = LocalDate.of(2026, 11, 1)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(today.minusDays(2), today, isCompleted = false)

        // THEN - The label says D+2
        assertEquals("D+2", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_returnsEmpty() {
        // GIVEN - An active task with no due date
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(null, today, isCompleted = false)

        // THEN - There is no label
        assertEquals("", label)
    }

    @Test
    fun dueDateLabel_completedWithDueDate_returnsClear() {
        // GIVEN - A completed task with a due date
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(today.plusDays(1), today, isCompleted = true)

        // THEN - The label says CLEAR
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_completedWithoutDueDate_returnsClear() {
        // GIVEN - A completed task with no due date
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Its due-date label is calculated
        val label = dueDateLabel(null, today, isCompleted = true)

        // THEN - The label says CLEAR
        assertEquals("CLEAR", label)
    }

    @Test
    fun dueDateLabel_activeWithoutDueDate_doesNotThrow() {
        // GIVEN - An active task with no due date and a valid reference date
        val today = LocalDate.of(2026, 10, 14)

        // WHEN / THEN - Calculating its label does not throw and returns the no-date result
        assertEquals("", dueDateLabel(null, today, isCompleted = false))
    }

    @Test
    fun dueDateLabel_adjacentDates_returnsBoundaryLabels() {
        // GIVEN - Today is 2026-10-14
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - Active tasks are due one day before, today, and one day after
        val labels = listOf(
            dueDateLabel(today.minusDays(1), today, isCompleted = false),
            dueDateLabel(today, today, isCompleted = false),
            dueDateLabel(today.plusDays(1), today, isCompleted = false)
        )

        // THEN - The boundary labels are overdue, today, and future
        assertEquals(listOf("D+1", "D-Day", "D-1"), labels)
    }

    @Test
    fun dueDateLabel_crossingMonthBoundary_countsAllCalendarDays() {
        // GIVEN - Today is 2026-10-14
        val today = LocalDate.of(2026, 10, 14)

        // WHEN - An active task is due 32 calendar days later, on 2026-11-15
        val label = dueDateLabel(LocalDate.of(2026, 11, 15), today, isCompleted = false)

        // THEN - The label uses the total calendar-day distance
        assertEquals("D-32", label)
    }

    @Test
    fun dueDateLabel_crossingYearBoundary_countsAllCalendarDays() {
        // GIVEN - Today is 2026-12-31
        val today = LocalDate.of(2026, 12, 31)

        // WHEN - An active task is due 31 calendar days later, on 2027-01-31
        val label = dueDateLabel(LocalDate.of(2027, 1, 31), today, isCompleted = false)

        // THEN - The label says D-31
        assertEquals("D-31", label)
    }

    @Test
    fun dueDateLabel_leapDay_countsFebruary29() {
        // GIVEN - Today is 2028-02-28 in a leap year
        val today = LocalDate.of(2028, 2, 28)

        // WHEN - An active task is due on 2028-03-01
        val label = dueDateLabel(LocalDate.of(2028, 3, 1), today, isCompleted = false)

        // THEN - February 29 is included in the distance
        assertEquals("D-2", label)
    }
}
