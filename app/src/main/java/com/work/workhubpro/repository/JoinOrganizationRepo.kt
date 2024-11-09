package com.work.workhubpro.repository

import com.work.workhubpro.api.WorkHubApi
import com.work.workhubpro.models.JoinOrganization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response
import retrofit2.await
import javax.inject.Inject

class JoinOrganizationRepo @Inject constructor(private val workHubApi: WorkHubApi) {
    private val _id = MutableStateFlow<Int>(0)
    val id: StateFlow<Int> get() = _id.asStateFlow()
    suspend fun join(request: JoinOrganization){
        val response = workHubApi.joinOrg(request)
        println(response.body())
        if (response.isSuccessful && response.body() != null) {
            println("hello finally")
        } else {
            // Handle error or null body case
            println("Error: Response not successful or body is null")
        }
    }

    suspend fun getID(request: Int){
        println("GetID Called")
        val response = workHubApi.getWorkHub(request)
        if (response.isSuccessful && response.body() != null) {
            println("hello finally")
            _id.emit(response.body()!!.toInt())
        } else {
            // Handle error or null body case
            println("Error: Response not successful or body is null")
        }

    }
}