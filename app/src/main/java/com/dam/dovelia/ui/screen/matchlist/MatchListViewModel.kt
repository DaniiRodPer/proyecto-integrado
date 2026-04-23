package com.dam.dovelia.ui.screen.matchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MatchListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state: MatchListState by mutableStateOf(MatchListState.Loading)
        private set


    fun loadMatches() {
        viewModelScope.launch {
            state = MatchListState.Loading

            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() }

            if (token.isNullOrEmpty()) {
                state = MatchListState.NoData
                return@launch
            }

            when (val result = userRepository.getMatches(token)) {
                is BaseResult.Success -> {
                    val matches = result.data
                    state = if (matches.isEmpty()) {
                        MatchListState.NoData
                    } else {
                        MatchListState.Success(dataSet = matches)
                    }
                }
                is BaseResult.Error -> {
                    state = MatchListState.NoData
                }
            }
        }
    }

    fun onDelete(userProfile: UserProfile) {

    }
}