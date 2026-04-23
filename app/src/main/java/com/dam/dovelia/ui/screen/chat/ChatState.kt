package com.dam.dovelia.ui.screen.chat

import com.dam.dovelia.data.model.Message
import com.dam.dovelia.data.model.UserProfile

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