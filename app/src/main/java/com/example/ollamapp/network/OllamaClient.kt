package com.example.ollamapp.network

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class OllamaClient(
    private val baseUrl: String = "http://localhost:11434",
    private val client: OkHttpClient = OkHttpClient()
) {

    suspend fun listModels(): List<String> {
        val request = Request.Builder()
            .url("$baseUrl/api/tags")
            .get()
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Failed to list models: ${response.code}")

        val json = JSONObject(response.body!!.string())
        val modelsArray = json.getJSONArray("models")
        return (0 until modelsArray.length()).map { modelsArray.getString(it) }
    }

    suspend fun pullModel(modelName: String, onProgress: (Float) -> Unit = {}) {
        val json = JSONObject().apply { put("name", modelName) }
        val requestBody = json.toString().toRequestBody(MediaType.get("application/json"))

        val request = Request.Builder()
            .url("$baseUrl/api/pull")
            .post(requestBody)
            .build()

        client.newCall(request).execute()
    }

    suspend fun chat(modelName: String, messages: List<ChatMessage>): List<ChatMessage> {
        val json = JSONObject().apply {
            put("model", modelName)
            put("messages", messages.map { mapToJson(it) })
            put("stream", false)
        }

        val request = Request.Builder()
            .url("$baseUrl/api/chat")
            .post(json.toString().toRequestBody(MediaType.get("application/json")))
            .build()

        val response = client.newCall(request).execute()
        if (!response.isSuccessful) throw Exception("Chat failed: ${response.code}")

        val respJson = JSONObject(response.body!!.string())
        val messagesArray = respJson.getJSONArray("messages")
        return (0 until messagesArray.length()).map {
            val obj = messagesArray.getJSONObject(it)
            ChatMessage(
                role = obj.getString("role"),
                content = obj.getString("content")
            )
        }
    }

    private fun JSONObject.mapToJson(msg: ChatMessage): Map<String, Any> = mapOf(
        "role" to msg.role,
        "content" to msg.content
    )
}

data class ChatMessage(val role: String, val content: String)

data class MessageEntity(
    val content: String,
    val role: String
)