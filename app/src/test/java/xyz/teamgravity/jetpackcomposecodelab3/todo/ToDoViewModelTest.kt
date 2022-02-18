package xyz.teamgravity.jetpackcomposecodelab3.todo

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import xyz.teamgravity.jetpackcomposecodelab3.util.generateRandomTodoItem

class ToDoViewModelTest {

    @Test
    fun `when removing item update list`() {
        // before
        val viewmodel = ToDoViewModel()

        val todo1 = generateRandomTodoItem()
        val todo2 = generateRandomTodoItem()

        viewmodel.addItem(todo1)
        viewmodel.addItem(todo2)

        // during
        viewmodel.removeItem(todo1)

        // after
        assertThat(viewmodel.todos).isEqualTo(listOf(todo2))
    }
}