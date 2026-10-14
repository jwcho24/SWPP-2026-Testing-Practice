# Spec: `DueDateUtils.kt`

## `dueDateLabel(dueDate: LocalDate?, today: LocalDate, isCompleted: Boolean)`

**Intent**: Return the task's D-day label for an optional due date relative to today's local calendar date. A completed task is labeled `CLEAR`; an active task with no due date has no label.

**Pre-conditions**
- The due date is either absent or a valid calendar date, and the completion state is supplied.
- Today and the due date use the same local calendar and time zone; the date difference is measured in calendar days, not elapsed 24-hour periods or day-of-month subtraction.
- Tests use a fixed reference date or clock so the expected label is deterministic.

**Post-conditions**
- Success cases
  - For an active task due today, return `D-Day`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueToday_returnsDDay`.
  - For an active task due one calendar day in the future, return `D-1`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueTomorrow_returnsD1`.
  - For an active task due three calendar days in the future, return `D-3`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeDueInThreeDays_returnsD3`.
  - For an active task overdue by two calendar days, return `D+2`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeOverdueByTwoDays_returnsDPlus2`.
  - For an active task with no due date, return the empty string `""`, which represents no label. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_activeWithoutDueDate_returnsEmpty`.
  - For a completed task with a due date, return `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithDueDate_returnsClear`.
  - For a completed task with no due date, return `CLEAR`. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_completedWithoutDueDate_returnsClear`.
- Error cases
  - None. Every supported input is valid.
- Edge cases
  - The calendar day immediately before today, today itself, and the day immediately after today must produce the three boundary labels. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_adjacentDates_returnsBoundaryLabels`.
  - Crossing a month boundary must count all calendar days, including when the interval contains a whole month component. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_crossingMonthBoundary_countsAllCalendarDays`.
  - Crossing a year boundary must count all calendar days. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_crossingYearBoundary_countsAllCalendarDays`.
  - Leap day is included in the calendar-day distance. `[Unit]`
    - Tested by `DueDateUtilsTest.dueDateLabel_leapDay_countsFebruary29`.

**Undefined behavior** (optional)
- Values outside the valid calendar-date domain are undefined; callers provide either a valid due date or no due date and a valid completion state.
