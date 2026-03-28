package com.dam.proydrp.ui.screen.matchlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dam.proydrp.data.mock.mockUserProfileList
import com.dam.proydrp.data.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchListViewModel @Inject constructor() : ViewModel() {

    var state: MatchListState by mutableStateOf(MatchListState.Loading)
        private set

    init {
        loadUsers()
    }

    private fun loadUsers() {
        viewModelScope.launch {
            delay(2000)
            state = if (mockUserProfileList.isEmpty()) {
                MatchListState.NoData
            } else {
                MatchListState.Success(dataSet = mockUserProfileList)
            }
        }
    }

    fun onDelete(userProfile: UserProfile) {

    }
}