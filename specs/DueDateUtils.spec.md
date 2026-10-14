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
    - Tip: Given today is 2026-10-14 and the active task is due 2026-10-14 / When `dueDateLabel` is called / Then it returns `D-Day`.
  - For an active task due one calendar day in the future, return `D-1`. `[Unit]`
    - Tip: Given today is 2026-10-14 and the active task is due 2026-10-15 / When `dueDateLabel` is called / Then it returns `D-1`.
  - For an active task due three calendar days in the future, return `D-3`. `[Unit]`
    - Tip: Given today is 2026-10-14 and the active task is due 2026-10-17 / When `dueDateLabel` is called / Then it returns `D-3`.
  - For an active task overdue by two calendar days, return `D+2`. `[Unit]`
    - Tip: Given today is 2026-11-01 and the active task was due 2026-10-30 / When `dueDateLabel` is called / Then it returns `D+2`.
  - For an active task with no due date, return the empty string `""`, which represents no label. `[Unit]`
    - Tip: Given today is 2026-10-14 and the active task has no due date / When `dueDateLabel` is called / Then it returns `""`.
  - For a completed task with a due date, return `CLEAR`. `[Unit]`
    - Tip: Given today is 2026-10-14 and a completed task is due 2026-10-15 / When `dueDateLabel` is called / Then it returns `CLEAR`.
  - For a completed task with no due date, return `CLEAR`. `[Unit]`
    - Tip: Given today is 2026-10-14 and a completed task has no due date / When `dueDateLabel` is called / Then it returns `CLEAR`.
- Error cases
  - None. An absent due date is valid; a valid due date must not cause an exception. `[Unit]`
    - Tip: Given an active task with no due date and a valid reference date of 2026-10-14 / When `dueDateLabel` is called / Then it returns `""` without throwing.
- Edge cases
  - The calendar day immediately before today, today itself, and the day immediately after today must produce the three boundary labels. `[Unit]`
    - Tip: Given today is 2026-10-14 / When active tasks are due 2026-10-13, 2026-10-14, and 2026-10-15 / Then the results are respectively `D+1`, `D-Day`, and `D-1`.
  - Crossing a month boundary must count all calendar days, including when the interval contains a whole month component. `[Unit]`
    - Tip: Given today is 2026-10-14 / When an active task is due 2026-11-15 / Then `dueDateLabel` returns `D-32`.
  - Crossing a year boundary must count all calendar days. `[Unit]`
    - Tip: Given today is 2026-12-31 and the active task is due 2027-01-31 / When `dueDateLabel` is called / Then it returns `D-31`.
  - Leap day is included in the calendar-day distance. `[Unit]`
    - Tip: Given today is 2028-02-28 and the active task is due 2028-03-01 / When `dueDateLabel` is called / Then it returns `D-2`.

**Undefined behavior** (optional)
- Values outside the valid calendar-date domain are undefined; callers provide either a valid due date or no due date and a valid completion state.
