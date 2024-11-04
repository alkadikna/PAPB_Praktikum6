package com.example.todoapp_room.data

import kotlinx.coroutines.flow.Flow

class OfflineItemsRepository(private val itemDao: TaskDao) : TasksRepository {
    override fun getAllTasksStream(): Flow<List<Task>> = itemDao.getAllTasks()

    override suspend fun insertTask(item: Task) = itemDao.insert(item)

    override suspend fun deleteTask(item: Task) = itemDao.delete(item)

    override suspend fun updateTask(item: Task) = itemDao.update(item)
}