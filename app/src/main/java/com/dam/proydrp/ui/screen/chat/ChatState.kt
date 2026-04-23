package com.dam.proydrp.ui.screen.chat

import com.dam.proydrp.data.model.Message
import com.dam.proydrp.data.model.UserProfile

sealed class ChatState {
    data object Loading : ChatState()
    data object NoData : ChatState()
    data class Success(
        val messages: List<Message> = emptyList(),
        val inputText: String = "",
        val targetUser: UserProfile,
        val myUserId: String
    ) : ChatState()
}