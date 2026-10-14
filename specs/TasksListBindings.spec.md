# Spec: `TasksListBindings.kt`

## `setDueDateLabel(...)`

**Intent**: Bind a task's due-date label to its task-row `TextView`. The visible text reflects the task's due date and completion state; when there is no label, the view is not displayed and stale text is not left visible after a row is reused.

**Pre-conditions**
- The target `TextView` belongs to a task row and is updated on the UI thread.
- The task has either no due date or a valid calendar date, and has a valid completion state.
- The label text follows `dueDateLabel(...)` in `DueDateUtils.spec.md`.
- Each bind uses the latest task state, including after completion is toggled.

**Post-conditions**
- Success cases
  - For an active task due tomorrow, display `D-1`. `[Integration]`
    - Tip: Given today is `LocalDate.now()` and the row is bound to an active task due `today.plusDays(1)` / When `setDueDateLabel` is called / Then the label is displayed and its text is `D-1`.
  - For an active task due today, display `D-Day`. `[Integration]`
    - Tip: Given today is `LocalDate.now()` and the row is bound to an active task due today / When `setDueDateLabel` is called / Then the label is displayed and its text is `D-Day`.
  - For an active task overdue by two calendar days, display `D+2`. `[Integration]`
    - Tip: Given today is `LocalDate.now()` and the row is bound to an active task due `today.minusDays(2)` / When `setDueDateLabel` is called / Then the label is displayed and its text is `D+2`.
  - For a completed task with a due date, display `CLEAR`. `[Integration]`
    - Tip: Given today is `LocalDate.now()` and the row is bound to a completed task due `today.plusDays(1)` / When `setDueDateLabel` is called / Then the label is displayed and its text is `CLEAR`.
  - For a completed task with no due date, display `CLEAR`. `[Integration]`
    - Tip: Given the row is bound to a completed task with no due date / When `setDueDateLabel` is called / Then the label is displayed and its text is `CLEAR`.
  - Rebinding a task as completed and then active recalculates its label from the latest state. `[Integration]`
    - Tip: Given today is `LocalDate.now()` and a task is due `today.plusDays(1)` / When the row is bound as active, completed, and active in that order / Then its displayed text changes `D-1` → `CLEAR` → `D-1`.
- Error cases
  - None. A task without a due date is valid, binding must not throw, and its label text is empty. `[Integration]`
    - Tip: Given an active task has no due date and the label view has empty text / When `setDueDateLabel` is called / Then it completes without throwing and the label text remains empty.
- Edge cases
  - Reusing a row that previously displayed a label for an active task without a due date must clear the previous text. `[Integration]`
    - Tip: Given a row displays `D-1` / When it is rebound to an active task with no due date / Then the label text is empty.
  - Toggling a task without a due date from active to completed and back must not leave a stale `CLEAR` label. `[Integration]`
    - Tip: Given an active task has no due date / When the row is bound as active, completed, and active / Then its text is empty, then `CLEAR`, then empty again.
  - A due date 32 calendar days later must display the full calendar-day distance even when that interval crosses a month boundary. `[Integration]`
    - Tip: Given today is `LocalDate.now()` and an active task is due `today.plusDays(32)` / When `setDueDateLabel` is called / Then the displayed text is `D-32`.

**Undefined behavior** (optional)
- Behavior for values outside the valid calendar-date domain or a view that is not a task-row label is undefined.
