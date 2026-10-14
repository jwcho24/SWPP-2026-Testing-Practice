# Spec: `TasksListBindings.kt`

## `setDueDateLabel(...)`

**Intent**: Bind the due-date label for a task row to its `TextView`. The displayed label must reflect the task's due date and completion state, and a task with no label must not leave stale text visible.

**Pre-conditions**
- The target `TextView` is attached to the task-row layout and is safe to update on the UI thread.
- The task's optional due date and completion state are valid.
- Label text follows the `dueDateLabel(...)` behavior specified in `DueDateUtils.spec.md`.

**Post-conditions**
- Success cases
  - For an active task due tomorrow, show `D-1` in the label view. `[Integration]`
    - Tip: Given today is 2026-10-14 and the row is bound to an active task due 2026-10-15 / When `setDueDateLabel` is called / Then the label is visible and its text is `D-1`.
  - For an active task due today, show `D-Day`; for an active task overdue by two calendar days, show `D+2`. `[Integration]`
    - Tip: Given today is 2026-10-14 / When rows are bound for active tasks due 2026-10-14 and 2026-10-12 / Then their visible labels are `D-Day` and `D+2` respectively.
  - For a completed task, show `CLEAR` whether it has a due date or not. `[Integration]`
    - Tip: Given today is 2026-10-14 / When rows are bound for completed tasks due 2026-10-15 and with no due date / Then both visible labels read `CLEAR`.
  - When a task changes from active to completed, update its label to `CLEAR`; when it changes back to active, recalculate the due-date label. `[Integration]`
    - Tip: Given today is 2026-10-14 and a task due 2026-10-15 / When the row is rebound first as completed and then as active / Then its label changes `D-1` → `CLEAR` → `D-1`.
- Error cases
  - None. An absent due date is valid and must be handled without throwing. `[Integration]`
    - Tip: Given an active task with no due date / When `setDueDateLabel` is called / Then it completes without throwing and leaves no label visible.
- Edge cases
  - When a recycled row previously showed a label but is rebound to an active task with no due date, clear the old text and hide the label view. `[Integration]`
    - Tip: Given a row showing `D-1` / When it is rebound to an active task with no due date / Then the text is cleared and the label view is `GONE`.
  - When a row with no due date is rebound to a completed task, show `CLEAR`; rebinding it to an active task with no due date hides the label again. `[Integration]`
    - Tip: Given today is 2026-10-14 / When the same row is bound to a completed task with no due date and then an active task with no due date / Then it shows `CLEAR` first and is `GONE` afterward.
  - Dates across a month boundary must retain the total calendar-day distance in the row label. `[Integration]`
    - Tip: Given today is 2026-10-31 and an active task is due 2026-11-02 / When `setDueDateLabel` is called / Then the visible text is `D-2`.

**Undefined behavior** (optional)
- Behavior for malformed dates or a `TextView` that is not attached to a task row is undefined.
