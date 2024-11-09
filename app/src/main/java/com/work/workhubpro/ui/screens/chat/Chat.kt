package com.work.workhubpro.ui.screens.chat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.Message
import com.work.workhubpro.ui.composables.MyTextField
import java.time.LocalTime

@Composable
fun Chat(receiver: String, navController: NavController, sharedViewModel: SharedViewModel) {
    var sender: String? = sharedViewModel.user.collectAsState().value?.username
    val chatViewModel: ChatViewModel = hiltViewModel()
    val receivechat = chatViewModel.receiverchat.collectAsState().value
    var senderchat by remember { mutableStateOf<Message?>(null) }
    LaunchedEffect(Unit) {
        chatViewModel.getmessages(sender!!, receiver)
        chatViewModel.connect("ws://10.0.2.2:8002/ws?room=$sender-$receiver")
        }
    val allMessages = chatViewModel.addmessages()
    val sortedMessages = allMessages.sortedBy { it.CreatedAt}

    var message by remember {
        mutableStateOf<String>("")
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(sortedMessages.size) { message ->
                val alignment = if (sortedMessages[message].sender == sender) Alignment.End else Alignment.Start
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .align(alignment)
                ) {
                    Text(

                        text = "${sortedMessages[message].sender}: ${sortedMessages[message].msg}",
                        color = if (sortedMessages[message].sender == sender) Color.Blue else Color.Green
                    )
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                MyTextField(
                    labelValue = stringResource(id = R.string.last_name),
                    painterResource(id = R.drawable.outline_edit_black_24dp),
                    textValue = message,
                    onValueChange = { message = it }
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {

                        val currentTime: LocalTime = LocalTime.now()
                        var msg = Message(message,"karan","abhi", currentTime)
                        senderchat = (msg)
                        chatViewModel.senderchat.value = senderchat
                        chatViewModel.sendmessage(message, sender!!, receiver)
                        message=""
                        senderchat= null
                    }
                ) {
                    Text(text = "Send")
                }
            }
        }
    }
}
