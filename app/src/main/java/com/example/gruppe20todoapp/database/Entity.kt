package com.example.gruppe20todoapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date


@Entity("tasks")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    @ColumnInfo("title")
    val title:String,
    @ColumnInfo("description")
    val description:String,
    @ColumnInfo("done")
    val done:Boolean = false,
    @ColumnInfo("added")
    val added:Long = System.currentTimeMillis(),
)

val TodoEntity.addDate:String get() = SimpleDateFormat("yyyy/MM/dd hh:mm").format(Date(added))
