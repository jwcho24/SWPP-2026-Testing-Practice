# Test Results Report

## Source and scope

Tests were written from the intent-verified specs against the course repository's `feature/due-date` source at commit `7e091a7` (`Set and clear due date on add/edit screen`). Production files were left unchanged. The test suite contains 11 JVM unit tests and 10 Android instrumentation tests.

## Local unit-test results

Command: `./gradlew testDebugUnitTest`

The due-date unit test class ran 11 tests: 9 passed and 2 failed. The existing statistics test also passed, so 12 tests ran in total. Both failures point to the calendar-distance bug described on Tutorial 07 page 68:

- `dueDateLabel_crossingMonthBoundary_countsAllCalendarDays`: expected `D-32`, got `D-1`. `Period.between(...).days` returns only the day component after the month component, rather than the total calendar-day distance.
- `dueDateLabel_crossingYearBoundary_countsAllCalendarDays`: expected `D-31`, got `D-Day`. The same day-component-only calculation loses the month portion across the year boundary.

The failures are expected for the intentionally buggy PR implementation; the tests preserve the required behavior as their oracle.

## Android integration tests

The 10 instrumentation tests compile successfully with `compileDebugAndroidTestKotlin`. The prior GitHub Actions run `38565161202` executed these tests plus one existing instrumentation test: 8 passed and 3 failed. All three failures match the intended due-date defects described on Tutorial 07 page 68:

- `setDueDateLabel_reusedRowWithoutDueDate_clearsStaleText`: expected an empty label, got `D-1`. A recycled row keeps the previous due-date text when the next task has no due date.
- `setDueDateLabel_dueIn32DaysAcrossMonth_displaysD32`: expected `D-32`, got `D-1`. The date-distance calculation keeps only the day component across a month boundary.
- `setDueDateLabel_undatedTaskCompletedThenReactivated_clearsStaleClear`: expected an empty label after reactivation, got `CLEAR`. The binding returns early for an empty label and leaves the prior completed-state text visible.

## CI summary

GitHub Actions run [38565161202](https://github.com/jwcho24/SWPP-2026-Testing-Practice/actions/runs/38565161202) completed with the expected failing status: unit tests ran 13 tests (11 passed, 2 failed) and integration tests ran 11 tests (8 passed, 3 failed). The five failing assertions correspond to the two bugs under test; unrelated existing tests passed. No production bug code was changed.
