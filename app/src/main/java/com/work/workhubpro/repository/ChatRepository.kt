package com.work.workhubpro.repository

import com.google.gson.Gson
import com.work.workhubpro.api.ChatApi
import com.work.workhubpro.api.Chatbody
import com.work.workhubpro.api.Messagedum
import com.work.workhubpro.models.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ChatRepository @Inject constructor(private val chatapi: ChatApi) {
    private var _getmessages = MutableStateFlow<List<Message>>(emptyList())
    val getmessages: StateFlow<List<Message>> get() = _getmessages.asStateFlow()

    suspend fun sendmessage(message: Message) {
        val response = chatapi.sendmessage(message)
        if (response.isSuccessful && response.body() != null) {

            println("success")
        } else {
            println("Error: Response not successful or body is null")
        }
    }
    fun getchat() : List<Message>{
        return getmessages.value
    }

    suspend fun getmessage(userinfo: Chatbody) {
        val response = chatapi.getmessages(userinfo)
        if (response.isSuccessful && response.body() != null) {

            val messages: List<Messagedum> = response.body()!!

            // Create a new list of Message objects with parsed CreatedAt field
            val chatmessages = mutableListOf<Message>()
            println("hello")
            println(messages)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
            messages.forEach { message ->
                val parsedTime = LocalTime.parse(message.CreatedAt?.format(formatter), formatter)
                val newMessage = Message(message.msg, message.sender, message.receiver, parsedTime)
                chatmessages.add(newMessage)
            }

            _getmessages.emit(chatmessages)
            println("NO ERROR")
        }
            else {
            println(response.errorBody()?.string())
                println(response.message())
                println("HAS SOME ERROR")
               }
    }
}


