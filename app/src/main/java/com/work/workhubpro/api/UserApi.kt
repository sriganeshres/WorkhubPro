package com.work.workhubpro.api

import com.work.workhubpro.models.LoginResponse
import com.work.workhubpro.models.Resp
import com.work.workhubpro.models.SendMail
import com.work.workhubpro.models.SignupResponse
import com.work.workhubpro.models.TokenRes
import com.work.workhubpro.models.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
data class employee(
val workhub_id: Int
)


interface UserApi {
    @POST("/api/signup")
    suspend fun signup(@Body request: User): Response<SignupResponse>

    @POST("/api/login")
    suspend fun login(@Body request: User): Response<LoginResponse>

    @POST("/api/token")
    suspend fun token(@Body request: String): Response<TokenRes>
    @POST("/api/sendmail")
    suspend fun mail(@Body request: SendMail): Response<Resp>

    @POST("/api/getallprojectleads")
    suspend fun getleaders(@Body workhub_id: employee): Response<List<User>>

    @POST("/api/getallemployees")
    suspend fun getemployees(@Body workhub_id: employee): Response<List<User>>


}
