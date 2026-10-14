package com.example.swpp.tododapp.tasks

import android.view.View
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.swpp.tododapp.data.Task
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksListBindingsTest {
    private val today: LocalDate
        get() = LocalDate.now()

    private fun labelView(): TextView =
        TextView(InstrumentationRegistry.getInstrumentation().targetContext)

    @Test
    fun setDueDateLabel_activeDueTomorrow_displaysD1() {
        // GIVEN - An active task due tomorrow
        val label = labelView()
        val task = Task(title = "Task", dueDate = today.plusDays(1))

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - D-1 is displayed
        assertEquals("D-1", label.text.toString())
        assertEquals(View.VISIBLE, label.visibility)
    }

    @Test
    fun setDueDateLabel_activeDueToday_displaysDDay() {
        // GIVEN - An active task due today
        val label = labelView()
        val task = Task(title = "Task", dueDate = today)

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - D-Day is displayed
        assertEquals("D-Day", label.text.toString())
        assertEquals(View.VISIBLE, label.visibility)
    }

    @Test
    fun setDueDateLabel_activeOverdueByTwoDays_displaysDPlus2() {
        // GIVEN - An active task whose due date was two days ago
        val label = labelView()
        val task = Task(title = "Task", dueDate = today.minusDays(2))

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - D+2 is displayed
        assertEquals("D+2", label.text.toString())
        assertEquals(View.VISIBLE, label.visibility)
    }

    @Test
    fun setDueDateLabel_completedWithDueDate_displaysClear() {
        // GIVEN - A completed task with a due date
        val label = labelView()
        val task = Task(title = "Task", isCompleted = true, dueDate = today.plusDays(1))

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - CLEAR is displayed
        assertEquals("CLEAR", label.text.toString())
        assertEquals(View.VISIBLE, label.visibility)
    }

    @Test
    fun setDueDateLabel_completedWithoutDueDate_displaysClear() {
        // GIVEN - A completed task without a due date
        val label = labelView()
        val task = Task(title = "Task", isCompleted = true)

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - CLEAR is displayed
        assertEquals("CLEAR", label.text.toString())
        assertEquals(View.VISIBLE, label.visibility)
    }

    @Test
    fun setDueDateLabel_rebindingActiveCompletedActive_usesLatestState() {
        // GIVEN - A task due tomorrow and a reusable label view
        val label = labelView()
        val dueDate = today.plusDays(1)

        // WHEN - The same row is bound as active, completed, then active
        setDueDateLabel(label, Task(title = "Task", dueDate = dueDate))
        val activeLabel = label.text.toString()
        setDueDateLabel(label, Task(title = "Task", isCompleted = true, dueDate = dueDate))
        val completedLabel = label.text.toString()
        setDueDateLabel(label, Task(title = "Task", dueDate = dueDate))

        // THEN - Its label follows each latest completion state
        assertEquals("D-1", activeLabel)
        assertEquals("CLEAR", completedLabel)
        assertEquals("D-1", label.text.toString())
    }

    @Test
    fun setDueDateLabel_activeWithoutDueDate_doesNotThrowAndHasEmptyText() {
        // GIVEN - An active task without a due date and an empty label view
        val label = labelView()
        val task = Task(title = "Task")

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - Binding succeeds and no label text is present
        assertTrue(label.text.isEmpty())
    }

    @Test
    fun setDueDateLabel_reusedRowWithoutDueDate_clearsStaleText() {
        // GIVEN - A row currently displaying D-1
        val label = labelView()
        setDueDateLabel(label, Task(title = "First", dueDate = today.plusDays(1)))
        assertEquals("D-1", label.text.toString())

        // WHEN - The row is rebound to an active task with no due date
        setDueDateLabel(label, Task(title = "Second"))

        // THEN - The previous label text is cleared
        assertEquals("", label.text.toString())
    }

    @Test
    fun setDueDateLabel_undatedTaskCompletedThenReactivated_clearsStaleClear() {
        // GIVEN - An active task with no due date
        val label = labelView()
        val active = Task(title = "Task")

        // WHEN - The same row is bound active, completed, then active again
        setDueDateLabel(label, active)
        val initiallyActiveLabel = label.text.toString()
        setDueDateLabel(label, active.copy(isCompleted = true))
        val completedLabel = label.text.toString()
        setDueDateLabel(label, active.copy(isCompleted = false))

        // THEN - CLEAR appears only while the task is completed
        assertEquals("", initiallyActiveLabel)
        assertEquals("CLEAR", completedLabel)
        assertEquals("", label.text.toString())
    }

    @Test
    fun setDueDateLabel_dueIn32DaysAcrossMonth_displaysD32() {
        // GIVEN - An active task due 32 calendar days from today
        val label = labelView()
        val task = Task(title = "Task", dueDate = today.plusDays(32))

        // WHEN - The task row's due-date label is bound
        setDueDateLabel(label, task)

        // THEN - The full calendar-day distance is displayed
        assertEquals("D-32", label.text.toString())
    }
}
