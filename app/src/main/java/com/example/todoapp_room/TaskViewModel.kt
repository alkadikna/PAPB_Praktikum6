package com.example.todoapp_room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp_room.data.Task
import com.example.todoapp_room.data.TasksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TaskViewModel(private val tasksRepository: TasksRepository): ViewModel(){
    val getAllTasks: Flow<List<Task>> = tasksRepository.getAllTasksStream()

    fun addTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.insertTask(task)
        }
    }

    fun updateTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(Dispatchers.IO) {
            tasksRepository.deleteTask(task)
        }
    }
}