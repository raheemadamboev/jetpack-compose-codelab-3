package xyz.teamgravity.jetpackcomposecodelab3.todo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.teamgravity.jetpackcomposecodelab3.util.generateRandomTodoItem
import kotlin.random.Random

/**
 * Stateless component that is responsible for the entire todo screen.
 *
 * @param items (state) list of [TodoItem] to display
 * @param onAddItem (event) request an item be added
 * @param onRemoveItem (event) request an item be removed
 */
@Composable
fun TodoScreen(
    items: List<TodoItem>,
    currentlyEditing: TodoItem?,
    onAddItem: (TodoItem) -> Unit,
    onRemoveItem: (TodoItem) -> Unit,
    onStartEdit: (TodoItem) -> Unit,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit
) {
    Column {
        val enableTopSection = currentlyEditing == null
        TodoItemInputBackground(elevate = enableTopSection) {
            if (enableTopSection) {
                TodoItemInputEntry(onAddItem)
            } else {
                Text(
                    "Editing item",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(top = 8.dp)
        ) {
            items(items = items) { todo ->
                if (currentlyEditing?.id == todo.id) {
                    TodoItemInlineEditor(
                        item = currentlyEditing,
                        onEditItemChange = onEditItemChange,
                        onEditDone = onEditDone,
                        onRemoveItem = { onRemoveItem(todo) }
                    )
                } else {
                    TodoRow(
                        todo,
                        { onStartEdit(it) },
                        Modifier.fillParentMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun TodoItemInlineEditor(
    item: TodoItem,
    onEditItemChange: (TodoItem) -> Unit,
    onEditDone: () -> Unit,
    onRemoveItem: () -> Unit
) = TodoItemInput(
    text = item.task,
    onTextChange = { onEditItemChange(item.copy(task = it)) },
    icon = item.icon,
    onIconChange = { onEditItemChange(item.copy(icon = it)) },
    submit = onEditDone,
    iconsVisible = true
) {
    Row {
        val shrinkButtons = Modifier.widthIn(20.dp)
        TextButton(onClick = onEditDone, modifier = shrinkButtons) {
            Text(
                text = "\uD83D\uDCBE", // floppy disk
                textAlign = TextAlign.End,
                modifier = Modifier.width(30.dp)
            )
        }
        TextButton(onClick = onRemoveItem, modifier = shrinkButtons) {
            Text(
                text = "❌",
                textAlign = TextAlign.End,
                modifier = Modifier.width(30.dp)
            )
        }
    }
}

/**
 * Stateless composable that displays a full-width [TodoItem].
 *
 * @param todo item to show
 * @param onItemClicked (event) notify caller that the row was clicked
 * @param modifier modifier for this element
 */
@Composable
fun TodoRow(
    todo: TodoItem,
    onItemClicked: (TodoItem) -> Unit,
    modifier: Modifier = Modifier,
    iconAlpha: Float = remember(key1 = todo.id) { randomTint() },
) {
    Row(
        modifier = modifier
            .clickable { onItemClicked(todo) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(todo.task)
        Icon(
            imageVector = todo.icon.imageVector,
            contentDescription = stringResource(id = todo.icon.contentDescription),
            tint = LocalContentColor.current.copy(alpha = iconAlpha)
        )
    }
}

private fun randomTint(): Float {
    return Random.nextFloat().coerceIn(0.3f, 0.9f)
}

@Preview
@Composable
fun PreviewTodoScreen() {
    val items = listOf(
        TodoItem("Learn compose", TodoIcon.Event),
        TodoItem("Take the codelab"),
        TodoItem("Apply state", TodoIcon.Done),
        TodoItem("Build dynamic UIs", TodoIcon.Square)
    )
    TodoScreen(items, null, {}, {}, {}, {}, {})
}

@Preview
@Composable
fun PreviewTodoRow() {
    val todo = remember { generateRandomTodoItem() }
    TodoRow(todo = todo, onItemClicked = {}, modifier = Modifier.fillMaxWidth())
}

@Composable
fun TodoItemInputEntry(onItemComplete: (TodoItem) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }
    val (icon, setIcon) = remember { mutableStateOf(TodoIcon.Default) }
    val iconsVisible = text.isNotBlank()
    val submit = {
        onItemComplete(TodoItem(text, icon))
        setIcon(TodoIcon.Default)
        setText("")
    }

    TodoItemInput(
        text = text,
        onTextChange = setText,
        iconsVisible = iconsVisible,
        onIconChange = setIcon,
        icon = icon,
        submit = submit
    ) {
        TodoEditButton(
            onClick = submit,
            text = "Add",
            enabled = text.isNotBlank()
        )
    }
}

@Composable
fun TodoItemInput(
    text: String,
    onTextChange: (String) -> Unit,
    iconsVisible: Boolean,
    onIconChange: (TodoIcon) -> Unit,
    icon: TodoIcon,
    submit: () -> Unit,
    buttonSlot: @Composable() () -> Unit
) {
    Column {
        Row(
            Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            TodoInputTextField(
                text = text,
                onTextChange = onTextChange,
                onImeAction = submit,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Box(Modifier.align(Alignment.CenterVertically)) { buttonSlot() }
        }

        if (iconsVisible) {
            AnimatedIconRow(icon, onIconChange, Modifier.padding(top = 8.dp))
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TodoInputTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier,
    onImeAction: () -> Unit
) {
    TodoInputText(
        text = text,
        onTextChange = onTextChange,
        modifier = modifier,
        onImeAction = onImeAction
    )
}