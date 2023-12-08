package com.example.gruppe20todoapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gruppe20todoapp.database.TodoEntity
import com.example.gruppe20todoapp.reps.TodoRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainVM:ViewModel(),KoinComponent {
    private val repo: TodoRepo by inject()

    private val _tasks:MutableStateFlow<List<TodoEntity>> = MutableStateFlow(emptyList())
    val tasks = _tasks.asStateFlow()

    init {
        getTodos()
    }

    private fun getTodos(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getTasks().collect{ data->
                _tasks.update { data }
            }
        }
    }

    fun updateTodo(todo: TodoEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.updateTasks(todo)
        }
    }
    fun deleteTodo(todo: TodoEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteTasks(todo)
        }
    }
    fun addTodo(todo: TodoEntity){
        viewModelScope.launch(Dispatchers.IO){
            repo.addTasks(todo)
        }
    }
}