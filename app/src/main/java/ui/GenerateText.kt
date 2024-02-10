package ui

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import util.hideKeyboard
import util.initChat
import util.sendMessage
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.Chat

@Composable
fun GenerateTextWithInput(generativeModel: GenerativeModel, activity: Activity) {
    var userInput by remember { mutableStateOf("") }
    var conversation by remember { mutableStateOf<List<Pair<String, Boolean>>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var chat by remember { mutableStateOf<Chat?>(null) }

    // Initialize or reinitialize the chat when the composable is first launched or the chat is reset
    LaunchedEffect(key1 = true) {
        initChat(generativeModel) { newChat ->
            chat = newChat
        }
    }

    fun resetConversation() {
        userInput = ""
        conversation = emptyList()
        isLoading = false
        initChat(generativeModel) { newChat ->
            chat = newChat
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Google AI",
                )
            }
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Reset conversation",
                modifier = Modifier.clickable { resetConversation() }
            )
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(conversation) { (message, isUser) ->
                Text(
                    text = if (isUser) "You: $message" else "Google AI: $message",
                    color = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Message Google AI...") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    if (userInput.isNotEmpty() && chat != null) {
                        val question = userInput
                        userInput = ""
                        isLoading = true
                        conversation = conversation + (question to true)
                        sendMessage(chat!!, question) { response ->
                            conversation = conversation + (response to false)
                            isLoading = false
                        }
                        activity.hideKeyboard()
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}
