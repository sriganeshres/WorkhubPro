package com.work.workhubpro.repository

import com.work.workhubpro.api.WorkHubApi
import com.work.workhubpro.models.Organisation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class OrganisationCreation @Inject constructor(private val workHubApi : WorkHubApi){
    private val _id = MutableStateFlow<Int>(0)
    private val _workhub =  MutableStateFlow<Organisation?>(null)
    val id: StateFlow<Int> get() = _id.asStateFlow()
    val workhub: StateFlow<Organisation?> get() = _workhub.asStateFlow()
    suspend fun getOrg(request: Organisation){
        val response = workHubApi.createorg(request)
        println(response.body()?.name)
        if (response.isSuccessful && response.body() != null) {
            response.body()!!.ID?.let { _id.emit(it) }
            println(_id.value) // Print the value of _id
            println("hello finally")
        } else {
            // Handle error or null body case
            println("Error: Response not successful or body is null")
        }
    }
    suspend fun getworkhub(id: String){
        println("Hey i am here")
        val response = workHubApi.getworkhub(id)
        if (response.isSuccessful && response.body() != null) {
            _workhub.emit(response.body()!!)
        } else {
            println("Error: Response not successful or body is null")
        }
    }


}