package com.example.gruppe20todoapp.reps

import com.example.gruppe20todoapp.database.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepo {
    suspend fun getTasks():Flow<List<TodoEntity>>
    suspend fun addTasks(todo: TodoEntity)
    suspend fun updateTasks(todo: TodoEntity)
    suspend fun deleteTasks(todo: TodoEntity)
}