package com.example.gruppe20todoapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface DAO {
    @Insert
    fun addTask(todo: TodoEntity)

    @Query("SELECT * FROM `tasks`")
    fun getTask():Flow<List<TodoEntity>>

    @Update
    fun updateTask(todo: TodoEntity)

    @Delete
    fun deleteTask(todo: TodoEntity)
}