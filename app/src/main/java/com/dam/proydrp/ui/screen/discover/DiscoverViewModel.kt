package com.dam.proydrp.ui.screen.discover

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.proydrp.data.mock.mockUserProfileList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor() : ViewModel() {

    var state by mutableStateOf<DiscoverState>(DiscoverState.Loading)
        private set

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            delay(2000)
            state = if (mockUserProfileList.isEmpty()) {
                DiscoverState.NoData
            } else {
                DiscoverState.Success(cards = mockUserProfileList)
            }
        }
    }

    fun onButtonPressed(isLike: Boolean) {
        val currentState = state
        if (currentState is DiscoverState.Success && !currentState.isSwipeLoading) {
            if (currentState.cards.isEmpty()) return

            state = currentState.copy(
                isSwipeLoading = true,
                swipeRightTrigger = isLike,
                swipeLeftTrigger = !isLike
            )
        }
    }

    fun onSwipe(isLike: Boolean) {
        val currentState = state
        if (currentState is DiscoverState.Success) {
            val remaining = currentState.cards.drop(1)

            state = if (remaining.isEmpty()) {
                DiscoverState.NoData
            } else {
                currentState.copy(
                    cards = remaining,
                    currentCard = remaining.firstOrNull(),
                    swipeLeftTrigger = false,
                    swipeRightTrigger = false,
                    isSwipeLoading = false
                )
            }

            if (isLike) {
                // Lógica de guardado de Like
            }
        }
    }
}