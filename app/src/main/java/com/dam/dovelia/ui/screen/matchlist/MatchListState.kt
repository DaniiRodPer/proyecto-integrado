package com.dam.dovelia.ui.screen.matchlist

import com.dam.dovelia.data.model.UserProfile

sealed class MatchListState {
    data object NoData: MatchListState()
    data object Loading: MatchListState()
    data class Success(
        val dataSet: List<UserProfile>,
    ): MatchListState()
}