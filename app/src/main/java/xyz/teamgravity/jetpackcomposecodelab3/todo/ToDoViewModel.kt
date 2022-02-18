package xyz.teamgravity.jetpackcomposecodelab3.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToDoViewModel : ViewModel() {

    var todos = mutableStateListOf<TodoItem>()
        private set

    private var currentEditPosition by mutableStateOf(-1)

    val currentEditItem: TodoItem?
        get() = todos.getOrNull(currentEditPosition)

    fun addItem(item: TodoItem) {
        todos.add(item)
    }

    fun removeItem(item: TodoItem) {
        todos.remove(item)
        onEditDone()
    }

    fun onEditItemSelected(item: TodoItem) {
        currentEditPosition = todos.indexOf(item)
    }

    // event: onEditDone
    fun onEditDone() {
        currentEditPosition = -1
    }

    // event: onEditItemChange
    fun onEditItemChange(item: TodoItem) {
        val currentItem = requireNotNull(currentEditItem)
        require(currentItem.id == item.id) {
            "You can only change an item with the same id as currentEditItem"
        }

        todos[currentEditPosition] = item
    }
}