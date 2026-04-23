package com.dam.dovelia.ui.screen.maincontainer

sealed class MainContainerState {
    data object Profile: MainContainerState()
    data object Discover: MainContainerState()
    data object ChatList: MainContainerState()
}