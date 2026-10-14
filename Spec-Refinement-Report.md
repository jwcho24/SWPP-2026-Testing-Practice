# Spec Refinement Report

## Intent sources

The refined specs follow the due-date feature intent in Tutorial 07, page 53, and the concrete bug scenarios on page 68. The two `-AI.spec.md` files preserve the original page 65 drafts before refinement.

## `DueDateUtils.spec.md`

- Made the date arithmetic explicitly use total local calendar-day distance. This addresses the page 68 bug where subtracting only day-of-month values gives an incorrect D-day count across month boundaries.
- Added exact before/today/after examples and month, year, and leap-day cases so the boundary behavior is testable with fixed dates.
- Specified the date utility's labels exactly: `D-N`, `D-Day`, and `D+N`; its active/no-date result is exactly `""` to represent no label.
- Matched the target function's contract: it receives completion state and returns `CLEAR` for completed tasks with or without a due date.
- Tagged direct date-label checks as `[Unit]`, because the function's contract is a value calculation.
- Moved the valid no-due-date behavior into the success cases and left error cases as `None`; the function accepts all supported inputs. Removed the duplicate no-due-date Tip/test and linked each remaining case to its test with `Tested by`.

## `TasksListBindings.spec.md`

- Split the UI expectations into individual test tips for active, completed, no-date, and boundary cases so each behavior has a clear test oracle.
- Required the label view to be undisplayed when an active task has no due date. This captures “no label” without requiring a specific Android visibility constant.
- Added row-reuse and completion-toggle cases. They catch the page 68 stale-label bug where `CLEAR` remains after an undated completed task is activated again.
- Required the adapter to use the latest bound task state and the date-label contract from `DueDateUtils.spec.md`.
- Tagged binding-adapter checks as `[Integration]`, because they exercise an Android `TextView` on the instrumentation runtime rather than a JVM-only value calculation.
- Moved the valid active/no-due-date behavior into success cases and left error cases as `None`. Linked each successful and edge behavior to the corresponding instrumentation test.

## Decisions to confirm during implementation

The user's fork `main` snapshot does not contain the due-date feature. The course repository's `feature/due-date` branch supplies the intended target functions and their actual Kotlin signatures. The tests use the explicit date argument for deterministic unit cases; the binding adapter uses the current date internally, so its tests express due dates relative to `LocalDate.now()`.
