package com.example.gruppe20todoapp.reps

import com.example.gruppe20todoapp.database.Database
import com.example.gruppe20todoapp.database.TodoEntity
import kotlinx.coroutines.flow.Flow

class TodoRepoImpl(private val database: Database): TodoRepo {
    private val dao = database.todoDao()

    override suspend fun getTasks(): Flow<List<TodoEntity>> = dao.getTask()
    override suspend fun addTasks(todo: TodoEntity) = dao.addTask(todo)
    override suspend fun updateTasks(todo: TodoEntity) = dao.updateTask(todo)
    override suspend fun deleteTasks(todo: TodoEntity) = dao.deleteTask(todo)
    override suspend fun getTasksByCompletion(isCompleted: Boolean): Flow<List<TodoEntity>> {
        return dao.getTasksByCompletionStatus(isCompleted)
    }


}