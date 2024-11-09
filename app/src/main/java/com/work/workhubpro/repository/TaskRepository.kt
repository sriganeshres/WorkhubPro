package com.work.workhubpro.repository

import com.work.workhubpro.api.WorkHubApi
import com.work.workhubpro.models.Task
import com.work.workhubpro.models.UpdateTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val workhubApi : WorkHubApi
){
    private val _id = MutableStateFlow<Int>(0)
    val id : StateFlow<Int> get() = _id.asStateFlow()
    private val _tasks= MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> get() = _tasks.asStateFlow()

    suspend fun createTask(request: Task) {
        val response = workhubApi.createTask(request)
        if (response.isSuccessful && response.body() != null){
            _id.emit(response.body()!!.ID)
        } else {
            println("Error: ${response.body()}")
        }
    }

    suspend fun deleteTask(request: Int) {
        val response = workhubApi.deleteTask(request)
        if (response.isSuccessful && response.body() != null){
             println("Successfully deleted")
        } else {
            println("Could not delete")
        }
    }




    suspend fun getTaskByUserID(request: Int) {
        val response = workhubApi.getTaskByUserID(request)
        if (response.isSuccessful && response.body() != null) {
            _tasks.emit(response.body()!!)
        } else {
            println("couldn't get")
        }
    }
    suspend fun getTaskToUserID(request: Int) {
        val response = workhubApi.getTaskToUserID(request)
        if (response.isSuccessful && response.body() != null) {
            _tasks.emit(response.body()!!)
        } else {
            println("couldn't get")
        }
    }

    suspend fun getTaskByProjectID(request: Int) {
        val response = workhubApi.getTaskByProjectID(request)
        if (response.isSuccessful && response.body() != null) {
            for (task in response.body()!!) {
                println(task.ID)
            }
        } else {
            println("couldn't get")
        }
    }
    suspend fun getTaskByWorkhubID(request: Int) {
        val response = workhubApi.getTaskByWorkhubID(request)
        if (response.isSuccessful && response.body() != null) {
            for (task in response.body()!!) {
                println(task.ID)
            }
        } else {
            println("couldn't get")
        }
    }

    suspend fun updateTask(request: UpdateTask) {
        val response = workhubApi.updateTask(request)
        println(request.taskID)
        if (response.isSuccessful && response.body() != null){
            println("Successfully updated")
        } else {
            println("Could not updated")
        }
    }
}