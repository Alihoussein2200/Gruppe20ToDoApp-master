package com.example.gruppe20todoapp.screens

import android.widget.ImageView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import  androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.example.gruppe20todoapp.database.TodoEntity
import com.example.gruppe20todoapp.database.addDate
import com.example.gruppe20todoapp.ui.theme.Dark100
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainVM = viewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    val tasks by viewModel.tasks.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var currentEditingItem by remember { mutableStateOf<TodoEntity?>(null) }
    //val groupedTasks by viewModel.groupedTasks.collectAsState()
    val gifUrl by viewModel.gifUrl.collectAsState()
    val (dialogOpen, setDialogOpen) = remember {
        mutableStateOf(false)
    }

    if (gifUrl != null) {
        DisplayGifDialog(gifUrl = gifUrl, onDismiss = { viewModel.clearGifUrl() })
    }

    if (showEditDialog) {
        EditTaskDialog(
            task = currentEditingItem,
            onDismiss = { showEditDialog = false },
            onConfirm = { title, description ->
                currentEditingItem?.let { todo ->
                    viewModel.editTodo(
                        todo = todo,
                        newTitle = title,
                        newDescription = description
                    )
                }
                showEditDialog = false
            }
        )
    }

    if (dialogOpen) {
        val (title, setTitle) = remember {
            mutableStateOf("")
        }
        val (description, setDesc) = remember {
            mutableStateOf("")
        }


        Dialog(onDismissRequest = { setDialogOpen(false) },
            ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Add Task",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(8.dp)

                )
                OutlinedTextField(
                    value = title,
                    onValueChange = setTitle,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title",  style = MaterialTheme.typography.bodyLarge) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        setDesc(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Description", style = MaterialTheme.typography.bodyLarge) },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.LightGray,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        if (title.isNotEmpty() && description.isNotEmpty()) {
                            viewModel.addTodo(
                                TodoEntity(
                                    title = title,
                                    description = description
                                )
                            )
                            setDialogOpen(false)
                        }
                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(text = "Submit", fontSize = 18.sp, color = Color.White, style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }



    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .shadow(4.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                TopAppBar(
                    title = { Text("Group 20 - MyTodoList APP", color = MaterialTheme.colorScheme.onPrimary) },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    setDialogOpen(true)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Task",
                    modifier = Modifier.size(30.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        containerColor = MaterialTheme.colorScheme.secondary

    ) { paddings ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)

            ) {
                SearchBar(
                    query = searchQuery,
                    onQueryChanged = { searchQuery = it },
                    onSearch = { viewModel.searchTasks(it) }
                )

                FilterButtons(viewModel = viewModel)

                AnimatedVisibility(
                    visible = tasks.isEmpty(),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {

                }
                AnimatedVisibility(
                    visible = tasks.isNotEmpty(),
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {

                }
                if (tasks.isEmpty()) {
                    Text(text = "No Tasks",
                        fontSize = 22.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(top = 200.dp))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = 8.dp,
                                bottom = paddings.calculateBottomPadding() + 8.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tasks.forEach { (date, taskList) ->
                            item {
                                TaskDateHeader(date)
                            }
                            items(taskList, key = { it.id }) { todo ->
                                TodoItem(todo = todo,
                                onClick = {
                                    viewModel.updateTodo(todo.copy(done = !todo.done))
                                },
                                onEdit = {
                                    currentEditingItem = todo
                                    showEditDialog = true
                                },
                                onDelete = {
                                    viewModel.deleteTodo(todo)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskDateHeader(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
    ) {
        Text(
            text = date,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
@Composable
fun DisplayGifWithGlide(gifUrl: String) {
    AndroidView(factory = { context ->
        ImageView(context).apply {
            Glide.with(context)
                .asGif()
                .load(gifUrl)
                .into(this)
        }
    }, modifier = Modifier.size(350.dp, 300.dp)) // Fixed size for the ImageView
}
@Composable
fun DisplayGifDialog(gifUrl: String?, onDismiss: () -> Unit) {
    if (gifUrl != null) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(300.dp)
                ) {
                    Text(
                        text = "Great Job!",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    DisplayGifWithGlide(gifUrl = gifUrl)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("Continue",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
            // Dismiss the dialog after a delay
            LaunchedEffect(Unit) {
                delay(10000) // 10000ms = 10 seconds
                onDismiss()
            }
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    focusManager: FocusManager = LocalFocusManager.current
) {
    OutlinedTextField(
        value = query,
        onValueChange = { updatedQuery ->
            onQueryChanged(updatedQuery)
            onSearch(updatedQuery)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        label = { Text("Search tasks", style = MaterialTheme.typography.bodyLarge) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        keyboardActions =
        KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = {
                    onQueryChanged("")
                    onSearch("")
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Clear search query",
                        tint = Color.White
                    )
                }
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.White,
            cursorColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White
        )
    )
}

@Composable
fun FilterButtons(viewModel: MainVM) {
    val filterState by viewModel.filterState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        FilterButton(
            text = "All",
            selected = filterState == MainVM.FilterState.ALL,
            onClick = { viewModel.showAllTasks() }
        )
        Spacer(Modifier.size(8.dp))
        FilterButton(
            text = "Completed",
            selected = filterState == MainVM.FilterState.COMPLETED,
            onClick = { viewModel.showCompletedTasks() }
        )
        Spacer(Modifier.size(8.dp))
        FilterButton(

            text = "Not Completed",
            selected = filterState == MainVM.FilterState.NOT_COMPLETED,
            onClick = { viewModel.showNotCompletedTasks() }
        )
    }
}

@Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Dark100 else MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text)
    }
}


@OptIn( ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.TodoItem(todo: TodoEntity, onClick: () -> Unit, onDelete: () -> Unit, onEdit: () -> Unit) {
    val color by animateColorAsState(
        targetValue = if (todo.done) Color(0xff24d65f) else Color(
            0xffff6363
        ), animationSpec = tween(500), label = ""
    )

    Box(
        modifier = Modifier.fillMaxWidth().animateItemPlacement(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ), contentAlignment = Alignment.BottomEnd
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(5.dp))
                .background(color)
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal = 8.dp,
                    vertical = 16.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        AnimatedVisibility(
                            visible = todo.done,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = color)
                        }
                    }
                    Row {
                        AnimatedVisibility(
                            visible = !todo.done,
                            enter = scaleIn() + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null, tint = color)
                        }
                    }
                }
                Column {
                    Text(
                        text = todo.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(text = todo.description, fontSize = 12.sp, color = Color(0xffebebeb))
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onEdit() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White

                    )
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onDelete() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White

                    )
                }


            }
        }

        Text(
            modifier = Modifier.padding(4.dp),
            text = todo.addDate,
            color = Color(0xffebebeb),
            fontSize = 10.sp
        )


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskDialog(
    task: TodoEntity?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    if (task == null) return

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Edit Task",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Title",  style = MaterialTheme.typography.bodyLarge) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "Description", style = MaterialTheme.typography.bodyLarge) },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.LightGray,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    shape = RoundedCornerShape(5.dp),
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onPrimary

                    )
                ) {
                    Text("Dismiss",style = MaterialTheme.typography.bodyLarge)
                }

                Button(
                    shape = RoundedCornerShape(5.dp),
                    onClick = { onConfirm(title, description) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Dark100,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Confirm",style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

