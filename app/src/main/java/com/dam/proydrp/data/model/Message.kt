package com.dam.proydrp.data.model

data class Message(
    val id: Int,
    val sender_id: String,
    val receiver_id: String,
    val text: String,
    val timestamp: String
)

data class MessageCreate(
    val receiver_id: String,
    val text: String
)