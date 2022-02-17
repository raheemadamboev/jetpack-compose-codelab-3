package xyz.teamgravity.jetpackcomposecodelab3.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.teamgravity.jetpackcomposecodelab3.ui.JetpackComposeCodelab3Theme

class ToDoActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewmodel = viewModel<ToDoViewModel>()
            val todos by viewmodel.todoItems.observeAsState(initial = emptyList())

            JetpackComposeCodelab3Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    TodoScreen(
                        items = todos,
                        onAddItem = viewmodel::addItem,
                        onRemoveItem = viewmodel::removeItem
                    )
                }
            }
        }
    }
}