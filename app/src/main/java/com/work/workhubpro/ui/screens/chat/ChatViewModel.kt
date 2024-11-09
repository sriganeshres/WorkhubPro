package com.work.workhubpro.ui.screens.chat

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.workhubpro.api.Chatbody
import com.work.workhubpro.models.Message
import com.work.workhubpro.repository.ChatRepository
import com.work.workhubpro.repository.WebSocketListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(private val webSocketListener: WebSocketListener,private val chatRepository: ChatRepository): ViewModel() {

    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    val senderchat = MutableLiveData<Message?>(null)
    var receiverchat = webSocketListener.message
    var receiverchat1 = webSocketListener._message
    val getmessages = chatRepository.getmessages
    var allMessages = mutableListOf<Message>()

    fun addmessages()
    : MutableList<Message>{
        println("i am debugging!!!!")
       if(senderchat.value!=null) {
           allMessages.add(senderchat.value!!)
           senderchat.value= null
       }
        val currenttime: LocalTime = LocalTime.now()
        if(receiverchat.value!="") {
            allMessages.add(Message(receiverchat.value, "abhi", "karan", currenttime))
            receiverchat1.value = ""
        }
        println(allMessages)
        return allMessages
    }
    fun connect(url : String){
    val request = Request.Builder()
        .url(url)
        .build()
        webSocket = okHttpClient.newWebSocket(request, webSocketListener)
    }
    fun sendmessage( message : String,sender:String,receiver: String){
        webSocket?.send(message)
        viewModelScope.launch {
            val chat = Message(message,sender,receiver)
            chatRepository.sendmessage(chat)
        }
    }
    fun getmessages( sender:String,receiver: String){
        println("i am in getmessages")
  var userinfo = Chatbody(sender,receiver)
  viewModelScope.launch {
      chatRepository.getmessage(userinfo)
      addmessages().addAll(getmessages.value)
  }
    }

}
