# Test Results Report

## Source and scope

Tests were written from the intent-verified specs against the course repository's `feature/due-date` source at commit `7e091a7` (`Set and clear due date on add/edit screen`). Production files were left unchanged. The test suite contains 12 JVM unit tests and 10 Android integration tests.

## Local unit-test results

Command: `./gradlew testDebugUnitTest`

The due-date unit test class ran 12 tests: 10 passed and 2 failed. The existing statistics test also passed. Both failures point to the calendar-distance bug described on Tutorial 07 page 68:

- `dueDateLabel_crossingMonthBoundary_countsAllCalendarDays`: expected `D-32`, got `D-1`. `Period.between(...).days` returns only the day component after the month component, rather than the total calendar-day distance.
- `dueDateLabel_crossingYearBoundary_countsAllCalendarDays`: expected `D-31`, got `D-Day`. The same day-component-only calculation loses the month portion across the year boundary.

The failures are expected for the intentionally buggy PR implementation; the tests preserve the required behavior as their oracle.

## Android integration tests

The 10 instrumentation tests compile successfully with `compileDebugAndroidTestKotlin`. They cover displayed labels, row reuse, and the active → completed → active transition for an undated task. The latter checks the stale `CLEAR` bug from page 68. CI results are pending.
