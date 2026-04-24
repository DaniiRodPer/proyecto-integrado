package com.dam.dovelia.ui.screen.matchlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.dam.dovelia.R
import com.dam.dovelia.data.mock.mockUserProfileList
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.components.AnimationComponent
import com.dam.dovelia.ui.components.CustomAlertDialog
import com.dam.dovelia.ui.components.ListItem
import com.dam.dovelia.ui.theme.ProydrpTheme

data class MatchListEvents(
    val onChatClick: (UserProfile) -> Unit,
    val onProfileClick: (String) -> Unit,
    val onDelete: (UserProfile) -> Unit
)

@Composable
fun MatchListScreen(
    scaffoldPadding: PaddingValues,
    onChatClick: (UserProfile) -> Unit,
    onNavigateToProfile: (String) -> Unit
) {
    val viewModel: MatchListViewModel = hiltViewModel()
    val currentState = viewModel.state
    val unreadUsers by viewModel.unreadUsers.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMatches()
    }

    val events = MatchListEvents(
        onChatClick = onChatClick,
        onProfileClick = onNavigateToProfile,
        onDelete = viewModel::onDelete,
    )

    when (currentState) {
        MatchListState.Loading -> {
            AnimationComponent(
                lottie = R.raw.loading_animation,
                text = stringResource(R.string.loading)
            )
        }

        MatchListState.NoData -> {
            AnimationComponent(
                lottie = R.raw.like_animation,
                loop = false,
                text = stringResource(R.string.no_match)
            )
        }

        is MatchListState.Success -> {
            MatchListContent(scaffoldPadding, currentState, events, unreadUsers)
        }

    }

}

@Composable
fun MatchListContent(
    scaffoldPadding: PaddingValues,
    state: MatchListState.Success,
    events: MatchListEvents,
    unreadUsers: Set<String>
) {
    val dimensions = LocalDimensions.current
    var deleteElement by remember { mutableStateOf<UserProfile?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(
                top = dimensions.huge,
                bottom = scaffoldPadding.calculateBottomPadding()
            ),
        contentAlignment = Alignment.TopCenter,
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensions.large),
            modifier = Modifier.padding(horizontal = dimensions.large)
        ) {
            items(state.dataSet) { userProfile ->
                ListItem(
                    onChatClick = events.onChatClick,
                    onNavigate = { events.onProfileClick(userProfile.id) },
                    onDelete = events.onDelete,
                    notification = unreadUsers.contains(userProfile.id),
                    userProfile = userProfile
                )
            }
        }
    }

    if (deleteElement != null) {
        CustomAlertDialog(
            title = R.string.delete,
            body = R.string.delete_match,
            primaryButtonText = R.string.confirm,
            secondaryButtonText = R.string.cancel,
            onConfirm = {
                events.onDelete(deleteElement!!)
                //onShowSnackbar("Se ha eliminado")
                deleteElement = null
            },
            onDismiss = { deleteElement = null },
            icon = R.drawable.delete_icon
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MatchListScreenPreview() {
    ProydrpTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            val events = MatchListEvents({}, {}, {})
            val state = MatchListState.Success(mockUserProfileList)

            MatchListContent(PaddingValues(), state, events, setOf())
        }
    }
}