package com.example.tecnomessager.data.model

data class Message(
    val id:Int? = null,
    val date: String? = null,
    val hour: String? = null,
    val contentMessage: String? = null,
    val file: String? = null,
    val userSend: String? = null,
    val userReceiver: String? = null
)