# Spec: `TasksListBindings.kt`

## `setDueDateLabel(...)`

**Intent**: Bind a task's due-date label to its task-row `TextView`. The text reflects the task's due date and completion state; when the computed label is empty, the text is cleared so no stale due-date label remains after a row is reused.

**Pre-conditions**
- The target `TextView` belongs to a task row and is updated on the UI thread.
- The task has either no due date or a valid calendar date, and has a valid completion state.
- The label text follows `dueDateLabel(...)` in `DueDateUtils.spec.md`.
- Each bind uses the latest task state, including after completion is toggled.

**Post-conditions**
- Success cases
  - For an active task due tomorrow, display `D-1`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeDueTomorrow_displaysD1`.
  - For an active task due today, display `D-Day`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeDueToday_displaysDDay`.
  - For an active task overdue by two calendar days, display `D+2`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeOverdueByTwoDays_displaysDPlus2`.
  - For a completed task with a due date, display `CLEAR`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithDueDate_displaysClear`.
  - For a completed task with no due date, display `CLEAR`. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_completedWithoutDueDate_displaysClear`.
  - Rebinding a task as completed and then active recalculates its label from the latest state. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_rebindingActiveCompletedActive_usesLatestState`.
  - For an active task with no due date, clear the label text without throwing. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_activeWithoutDueDate_doesNotThrowAndHasEmptyText`.
- Error cases
  - None. Tasks with an absent due date and valid completion state are supported inputs.
- Edge cases
  - Reusing a row that previously displayed a label for an active task without a due date must clear the previous text. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_reusedRowWithoutDueDate_clearsStaleText`.
  - Toggling a task without a due date from active to completed and back must not leave a stale `CLEAR` label. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_undatedTaskCompletedThenReactivated_clearsStaleClear`.
  - A due date 32 calendar days later must display the full calendar-day distance even when that interval crosses a month boundary. `[Integration]`
    - Tested by `TasksListBindingsTest.setDueDateLabel_dueIn32DaysAcrossMonth_displaysD32`.

**Undefined behavior** (optional)
- Behavior for values outside the valid calendar-date domain or a view that is not a task-row label is undefined.
