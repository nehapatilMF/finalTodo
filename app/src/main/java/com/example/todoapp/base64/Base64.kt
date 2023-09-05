package com.example.todoapp.base64
import android.util.Base64
object Base64 {
    fun encodeToBase64(data: ByteArray): String {
        return Base64.encodeToString(data, Base64.DEFAULT)
    }

    // Encode a string to Base64
    fun encodeToBase64(text: String): String {
        val data = text.toByteArray(Charsets.UTF_8)
        return encodeToBase64(data)
    }
    fun decodeFromBase64(encodedData: String): ByteArray {
        return Base64.decode(encodedData, Base64.DEFAULT)
    }

    // Decode a Base64 encoded string to a string
    fun decodeFromBase64ToString(encodedData: String): String {
        val decodedBytes = decodeFromBase64(encodedData)
        return String(decodedBytes, Charsets.UTF_8)
    }
}