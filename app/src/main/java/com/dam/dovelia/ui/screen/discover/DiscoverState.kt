package com.dam.dovelia.ui.screen.discover

import com.dam.dovelia.data.model.UserProfile

sealed class DiscoverState {
    data object NoData: DiscoverState()
    data object Loading: DiscoverState()
    data class Success(
        val cards: List<UserProfile> = emptyList(),
        val currentCard: UserProfile? = null,
        val isSwipeLoading: Boolean = false,
        val swipeRightTrigger: Boolean = false,
        val swipeLeftTrigger: Boolean = false,
        val matchUser: UserProfile? = null,
        val showMatchAnimation: Boolean = false
    ): DiscoverState()
}
