package com.work.workhubpro.api

import com.work.workhubpro.models.Message
import com.work.workhubpro.models.Organisation
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.LocalTime

data class Chatbody (
         var sender: String,
          var receiver : String,
         )
data class Messagedum (
    val msg: String,
    val sender: String,
    val receiver: String,
    var CreatedAt: String?=null,
    val ID: Int?= null,

    )


interface ChatApi {
    @POST("/api/sendmessage")
    suspend fun sendmessage(@Body request: Message): Response<String>

    @POST("/api/getmessage")
    suspend fun getmessages(@Body request: Chatbody): Response<List<Messagedum>>
}