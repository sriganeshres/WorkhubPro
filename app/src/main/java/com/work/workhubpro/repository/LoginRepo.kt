package com.work.workhubpro.repository

import com.work.workhubpro.api.UserApi
import com.work.workhubpro.models.LoginReq
import com.work.workhubpro.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


class LoginRepo @Inject constructor(private val userApi: UserApi) {

    private val _user = MutableStateFlow<User?>(null)
    private val _token = MutableStateFlow<String>("")
    val token: StateFlow<String> get()= _token.asStateFlow()
    val user: StateFlow<User?> get()= _user.asStateFlow()
    suspend fun getUser(request: User) {
        println("heroku")
        val response = userApi.login(request)

        if (response.isSuccessful && response.body() != null) {
            println("successfully got login info")
            println(response.body())
            _token.emit(response.body()!!.token)
            _user.emit(response.body()!!.user)
        }
        else{
            println(response.errorBody())
        }
    }

}