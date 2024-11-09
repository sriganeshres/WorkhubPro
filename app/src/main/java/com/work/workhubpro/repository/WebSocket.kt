
package com.work.workhubpro.repository
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

open class  WebSocketListener @Inject constructor(

): WebSocketListener(){
    var _message = MutableStateFlow<String>("")
    val message: StateFlow<String> get() = _message.asStateFlow()
    val TAG="Testing"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(TAG, "onOpen: Connected to the Server")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
       _message.value = text
        Log.d(TAG, "onMessage: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {

        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)

        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}