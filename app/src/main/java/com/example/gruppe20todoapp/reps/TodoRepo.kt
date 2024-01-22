package com.example.gruppe20todoapp.reps

import com.example.gruppe20todoapp.api.GiphyResponse
import com.example.gruppe20todoapp.database.TodoEntity
import kotlinx.coroutines.flow.Flow

interface TodoRepo {
    suspend fun getTasks():Flow<List<TodoEntity>>
    suspend fun addTasks(todo: TodoEntity)
    suspend fun updateTasks(todo: TodoEntity)
    suspend fun deleteTasks(todo: TodoEntity)
    suspend fun searchTasks(searchQuery: String): Flow<List<TodoEntity>>
    fun getAllTasks(): Flow<List<TodoEntity>>
    fun getCompletedTasks(): Flow<List<TodoEntity>>
    fun getNotCompletedTasks(): Flow<List<TodoEntity>>



}