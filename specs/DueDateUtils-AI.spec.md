# Spec: `DueDateUtils.kt`

## `dueDateLabel(...)`

**Intent**: Return the due-date label for an active task using the difference between the task's due calendar date and today's local calendar date. A completed task is labeled `CLEAR`, regardless of its due date.

**Pre-conditions**
- The due date is either absent or a valid calendar date.
- The reference date and due date are interpreted in the same local calendar and time zone. Tests must use a fixed reference date so results are deterministic.

**Post-conditions**
- Success cases
  - For an active task due today, return `D-Day`. `[Unit]`
    - Tip: Given today is 2026-10-14 and the active task is due 2026-10-14 / When `dueDateLabel` is called / Then it returns `D-Day`.
  - For an active task due in the future, return `D-N`, where `N` is the total number of calendar days until the due date. `[Unit]`
    - Tip: Given today is 2026-10-14 and the active task is due 2026-10-15 / When `dueDateLabel` is called / Then it returns `D-1`.
  - For an active task whose due date is in a later month, count all calendar days across the month boundary. `[Unit]`
    - Tip: Given today is 2026-10-31 and the active task is due 2026-11-02 / When `dueDateLabel` is called / Then it returns `D-2`.
  - For an active task overdue by `N` calendar days, return `D+N`. `[Unit]`
    - Tip: Given today is 2026-11-01 and the active task was due 2026-10-30 / When `dueDateLabel` is called / Then it returns `D+2`.
  - For an active task with no due date, return an empty string so the UI can show no label. `[Unit]`
    - Tip: Given today is 2026-10-14 and the active task has no due date / When `dueDateLabel` is called / Then it returns `""`.
  - For a completed task, return `CLEAR` whether its due date is present or absent. `[Unit]`
    - Tip: Given today is 2026-10-14 and a completed task is due 2026-10-15 / When `dueDateLabel` is called / Then it returns `CLEAR`; repeat with no due date and expect `CLEAR`.
- Error cases
  - None. An absent due date and either completion state are valid inputs. `[Unit]`
    - Tip: Given a task with no due date / When `dueDateLabel` is called / Then it returns the valid no-date result for the task's completion state and does not throw.
- Edge cases
  - The day before, the due date itself, and the day after form the exact boundary between overdue, due today, and future labels. `[Unit]`
    - Tip: Given today is 2026-10-14 / When an active task is due 2026-10-13, 2026-10-14, or 2026-10-15 / Then the results are respectively `D+1`, `D-Day`, and `D-1`.
  - A month boundary must use total calendar-day distance, not subtraction of day-of-month values. `[Unit]`
    - Tip: Given today is 2026-10-31 / When an active task is due 2026-11-01 / Then it returns `D-1`.

**Undefined behavior** (optional)
- Behavior for malformed or unrepresentable date values is undefined; callers must provide a valid date or no due date.
