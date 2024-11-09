package com.work.workhubpro.repository

import com.work.workhubpro.api.WorkHubApi
import com.work.workhubpro.models.Project
import com.work.workhubpro.models.Resp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class Projectcreation @Inject constructor(private val workHubApi: WorkHubApi) {
    private var _response = MutableStateFlow<Resp?>(null)
    private var _projects = MutableStateFlow<List<Project>>(emptyList())
    private var _parProject = MutableStateFlow<Project?>(null)
    val response: StateFlow<Resp?> get() = _response.asStateFlow()
    val projects: StateFlow<List<Project>> get() = _projects.asStateFlow()
    val currProject: StateFlow<Project?> get() = _parProject.asStateFlow()


    suspend fun getProject(request: Project) {
        val response = workHubApi.createProject(request)

        if (response.isSuccessful && response.body() != null) {
           _response.emit(response.body()) // Print the value of _id
            println("hello finally")
        } else {
            // Handle error or null body case
            println("Error: Response not successful or body is null")
        }
    }
    suspend fun getallProjects(id: String) {
        val response = workHubApi.getallprojects(id)

        if (response.isSuccessful && response.body() != null) {
            _projects.emit(response.body()!!) // Print the value of _id
            println("hello finally")
        } else {
            // Handle error or null body case
            println("Error: Response not successful or body is null")
        }
    }

    suspend fun getProjectDetails(request: Int) {
        val response = workHubApi.getProjectById(request)
        if (response.isSuccessful && response.body() != null) {
            _parProject.emit(response.body()!!)
            println("i am getting the project")
            println(response.body())
        } else {
            println("Error: Response not successful or body is null")

        }
    }

}