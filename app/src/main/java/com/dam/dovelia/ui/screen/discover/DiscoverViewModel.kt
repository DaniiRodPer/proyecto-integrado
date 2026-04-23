package com.dam.dovelia.ui.screen.discover

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.dovelia.data.network.BaseResult
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    var state by mutableStateOf<DiscoverState>(DiscoverState.Loading)
        private set

    private var loadedToken: String? = null
    private var currentCity: String? = null
    private var currentRooms: Int? = null
    private var currentBathrooms: Int? = null

    fun verifySessionAndLoad(city: String?, rooms: Int?, bathrooms: Int?, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() }

            val tokenChanged = token != loadedToken
            val filtersChanged = city != currentCity || rooms != currentRooms || bathrooms != currentBathrooms

            if (tokenChanged || filtersChanged || forceRefresh) {
                loadedToken = token
                currentCity = city
                currentRooms = rooms
                currentBathrooms = bathrooms

                loadUsers(city, rooms, bathrooms)
            }
        }
    }
    fun loadUsers(city: String? = null, rooms: Int? = null, bathrooms: Int? = null) {
        viewModelScope.launch {
            state = DiscoverState.Loading

            delay(1500)

            val token = withContext(Dispatchers.IO) {
                sessionManager.getAuthToken()
            }

            if (token.isNullOrEmpty()) {
                state = DiscoverState.NoData
                return@launch
            }

            when (val result = userRepository.getDiscoverUsers(token, city, rooms, bathrooms)) {
                is BaseResult.Success -> {
                    val users = result.data
                    state = if (users.isEmpty()) {
                        DiscoverState.NoData
                    } else {
                        DiscoverState.Success(
                            cards = users,
                            currentCard = users.firstOrNull()
                        )
                    }
                }

                is BaseResult.Error -> {
                    state = DiscoverState.NoData
                }
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
        val currentState = state as? DiscoverState.Success ?: return
        val swipedUser = currentState.currentCard ?: return
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

        viewModelScope.launch {
            val token = withContext(Dispatchers.IO) { sessionManager.getAuthToken() } ?: return@launch

            when (val result = userRepository.swipeUser(token, swipedUser.id, isLike)) {
                is BaseResult.Success -> {
                    if (result.data.mutual_match) {

                        val latestState = state as? DiscoverState.Success ?: return@launch

                        state = latestState.copy(
                            matchUser = swipedUser,
                            showMatchAnimation = true
                        )

                        delay(4000)

                        val afterDelayState = state as? DiscoverState.Success
                        if (afterDelayState != null) {
                            state = afterDelayState.copy(
                                showMatchAnimation = false,
                                matchUser = null
                            )
                        }
                    }
                }

                is BaseResult.Error -> {}
            }
        }
    }
}