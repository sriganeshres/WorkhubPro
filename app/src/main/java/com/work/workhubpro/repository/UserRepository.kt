package com.work.workhubpro.repository

import com.work.workhubpro.api.UserApi
import com.work.workhubpro.di.NetworkModule
import com.work.workhubpro.models.SendMail
import com.work.workhubpro.api.employee
import com.work.workhubpro.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class UserRepository @Inject constructor(private val userapi: UserApi) {
    private val _user = MutableStateFlow<User?>(null)
    private val _token = MutableStateFlow<String>("")
    private val _projectleaders = MutableStateFlow<List<User>?>(null)
    private val _employees = MutableStateFlow<List<User>?>(null)
    private val _success = MutableStateFlow<Boolean?>(null)

    val user: StateFlow<User?> get() = _user.asStateFlow()
    val token: StateFlow<String> get() = _token.asStateFlow()
    val projectleaders: StateFlow<List<User>?> get() = _projectleaders.asStateFlow()
    val employees: StateFlow<List<User>?> get() = _employees.asStateFlow()
  val success : StateFlow<Boolean?> = _success.asStateFlow()
    suspend fun getUser(request: User) {
        val response = userapi.signup(request)
        if (response.isSuccessful && response.body() != null) {
            _user.emit(response.body()!!.user)
            _token.emit(response.body()!!.token)
            println(response.body())
        }
        else{
            println("some error")
        }
    }
    suspend fun user_from_token(token : String){
        val response = userapi.token(token)
        if (response.isSuccessful && response.body() != null) {
            _user.emit(response.body()!!.user)
            _success.emit(response.body()!!.success)
        }
        else{
            _success.emit(false)
        }
    }

    suspend fun projectLead_from_workhub(workhub_id:Int){
        val employee = employee(workhub_id)
        val response = userapi.getleaders(employee)
        if (response.isSuccessful && response.body() != null) {
            _projectleaders.emit(response.body())
        }
        else{
            println("some error")
            println(response.errorBody())
        }
    }

    suspend fun employees_from_workhub(workhub_id: Int){
        val employee= employee(workhub_id)
        val response = userapi.getemployees(employee)
        if (response.isSuccessful && response.body() != null) {
            _employees.emit(response.body())
        }
        else{
            println("some error is occuring")
            println(response.errorBody())
        }
    }
    suspend fun sendemail(request: SendMail){
        val response = userapi.mail(request)
        if (response.isSuccessful && response.body() != null) {
           println(response.body())
        }
        else{
            println("some error")
        }
    }
}