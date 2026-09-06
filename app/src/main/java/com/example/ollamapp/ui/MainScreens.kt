package com.example.ollamapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ollamapp.MainActivity
import com.example.ollamapp.network.ChatMessage
import com.example.ollamapp.network.OllamaClient
import kotlinx.coroutines.launch

val ChatScreen by composable(navController: NavController) {
    private val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    var userInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().verticalScroller(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterStart
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Ollama Chat",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(8.dp))
        MessagesList(messages)
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = userInput,
                onValueChange = { userInput = it },
                label = { Text("Message") },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                trailingIcon = {
                    if (userInput.isNotEmpty()) {
                        Button(
                            onClick = {
                                viewModel.addMessage(
                                    role = "user",
                                    content = userInput
                                )
                                userInput = ""
                            }
                        ) {
                            Text("Send")
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MessagesList(messages: List<ChatMessage>) {
    messages.forEach { message ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = if (message.role == "user") {
                Arrangement.End
            } else {
                Arrangement.Start
            }
        ) {
            Box(
                modifier = Modifier
                    .background(
                        if (message.role == "user") {
                            Color.Unspecified
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                    .maxWidth(0.7f)
                    .maxHeight(150.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.body2,
                    color = if (message.role == "user") {
                        Color.Unspecified
                    } else {
                        Color.White
                    },
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun CodingAgentScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterStart
    ) {
        Text(
            text = "On-device Coding Agent",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Enter task description") },
            modifier = Modifier.fillMaxWidth(0.8f).padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                // TODO: trigger coding agent
            }
        ) {
            Text("Generate App")
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                navController.navigate("chat")
            }
        ) {
            Text("Back to Chat")
        }
    }
}

class ChatViewModel : ViewModel() {
    private val _messages = mutableStateOf<List<ChatMessage>>(listOf())
    val messages: State<List<ChatMessage>> = _messages

    private val client = OllamaClient()

    fun addMessage(role: String, content: String) {
        viewModelScope.launch {
            val newMessage = ChatMessage(content = content, role = role)
            val updated = _messages.value + newMessage
            _messages.value = updated

            try {
                val responseMessages = client.chat(
                    modelName = "llama2",
                    messages = updated
                )
                val assistant = responseMessages
                    .filter { it.role == "assistant" }
                    .map { it.content }
                    .firstOrNull() ?: "I'm not sure how to respond to that."

                val assistantMessage = ChatMessage(content = assistant, role = "assistant")
                val finalUpdated = _messages.value + assistantMessage
                _messages.value = finalUpdated
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    content = "Error: ${e.message}",
                    role = "assistant"
                )
            }
        }
    }
}