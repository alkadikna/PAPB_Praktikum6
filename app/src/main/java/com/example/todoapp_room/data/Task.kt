package com.example.todoapp_room.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    var isChecked: Boolean = false
//    val isChecked: MutableState<Boolean> =
//        mutableStateOf(false)
)
