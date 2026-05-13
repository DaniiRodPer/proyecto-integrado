package com.dam.dovelia.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dam.dovelia.R
import com.dam.dovelia.data.model.UserProfile
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.common.TopBarConfig
import com.dam.dovelia.ui.components.CustomAlertDialog
import com.dam.dovelia.ui.components.images.ProfilePic
import com.dam.dovelia.ui.components.text.Title
import com.dam.dovelia.ui.screen.AboutUsScreen
import com.dam.dovelia.ui.screen.chat.ChatScreen
import com.dam.dovelia.ui.screen.matchlist.MatchListScreen
import com.dam.dovelia.ui.screen.discover.DiscoverScreen
import com.dam.dovelia.ui.screen.editprofile.EditProfileScreen
import com.dam.dovelia.ui.screen.filterselection.FilterSelectionScreen
import com.dam.dovelia.ui.screen.login.LoginScreen
import com.dam.dovelia.ui.screen.profile.ProfileScreen
import com.dam.dovelia.ui.screen.recoverpassword.RecoverPasswordScreen
import com.dam.dovelia.ui.screen.register.RegisterScreen

object Routes {
    const val PROFILE = "profile"
    const val OTHER_PROFILE = "other_profile"
    const val DISCOVER = "discover"
    const val MATCHLIST = "matchlist"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val EDITPROFILE = "editprofile"
    const val REGISTER_STEP_2 = "register_step_2"
    const val FILTERS = "filters"
    const val CHAT = "chat"
    const val RECOVER_PASSWORD = "recover_password"
    const val ABOUT_US = "about_us"
}

data class DialogConfig(
    val title: Int,
    val body: Int,
    val primaryButtonText: Int,
    val secondaryButtonText: Int,
    val icon: Int,
    val mode: Boolean,
    val onConfirm: () -> Unit,
    val onDismiss: () -> Unit
)


@Composable
fun NavHostScreen(
    navController: NavHostController,
    scaffoldPadding: PaddingValues,
    topBarConfig: (TopBarConfig) -> Unit,
    sessionManager: SessionManager,
    startDestination: String,
    onLogout: () -> Unit
    //onConfigureTopBar: (BaseTopAppBarState) -> Unit,
    //onOpenDrawer: () -> Unit,
    //onConfigureFab: (FloatingActionButtonState) -> Unit,
    //onEnableDrawerGestures: (Boolean) -> Unit,
    //onShowSnackbar: (String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var activeDialog by remember { mutableStateOf<DialogConfig?>(null) }

    activeDialog?.let { config ->
        CustomAlertDialog(
            title = config.title,
            body = config.body,
            primaryButtonText = config.primaryButtonText,
            secondaryButtonText = config.secondaryButtonText,
            icon = config.icon,
            mode = config.mode,
            onDismiss = config.onDismiss,
            onConfirm = config.onConfirm
        )
    }

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Routes.PROFILE -> {
                topBarConfig(
                    TopBarConfig(
                        show = true,
                        leadingIcon = R.drawable.logout_icon,
                        onLeadingClick = {
                            activeDialog = DialogConfig(
                                title = R.string.logout_title,
                                body = R.string.logout_warning,
                                primaryButtonText = R.string.confirm,
                                secondaryButtonText = R.string.cancel,
                                icon = R.drawable.logout_icon,
                                mode = true,
                                onConfirm = {
                                    sessionManager.clearSession()
                                    onLogout()
                                    activeDialog = null
                                    navController.navigate(Routes.LOGIN) {
                                        popUpTo(0) {
                                            inclusive = true
                                        } //Cleans de back button previous screens
                                    }
                                },
                                onDismiss = { activeDialog = null }
                            )
                        },
                        trailingIcon = R.drawable.edit_icon,
                        onTrailingClick = { navController.navigate(Routes.EDITPROFILE) }
                    ))
            }

            Routes.OTHER_PROFILE -> {
                topBarConfig(
                    TopBarConfig(
                        show = true,
                        leadingIcon = R.drawable.arrow_back_icon,
                        onLeadingClick = { navController.popBackStack() },
                    )
                )
            }

            Routes.DISCOVER -> {
                topBarConfig(
                    TopBarConfig(
                        true,
                        leadingIcon = R.drawable.info_icon,
                        onLeadingClick = {navController.navigate(Routes.ABOUT_US)},
                        trailingIcon = R.drawable.sort_icon,
                        onTrailingClick = { navController.navigate(Routes.FILTERS) },
                        titleAlignment = TextAlign.Center,
                        customContent = {
                            Title(
                                stringResource(R.string.discover_title),
                                fontSize = 36.sp
                            )
                        }
                    ))
            }

            Routes.MATCHLIST -> {
                topBarConfig(
                    TopBarConfig(
                        true,
                        titleAlignment = TextAlign.Center,
                        customContent = {
                            Title(
                                stringResource(R.string.nav_match_desc),
                                fontSize = 36.sp
                            )
                        }
                    ))
            }

            Routes.LOGIN, Routes.REGISTER, Routes.RECOVER_PASSWORD -> {
                topBarConfig(TopBarConfig(show = false))
            }

            Routes.EDITPROFILE -> {
                topBarConfig(
                    TopBarConfig(
                        true,
                        title = R.string.edit_profile_title,
                        leadingIcon = R.drawable.arrow_back_icon,
                        onLeadingClick = { navController.popBackStack() },
                    )
                )
            }

            Routes.REGISTER_STEP_2 -> {
                topBarConfig(
                    TopBarConfig(
                        true,
                        title = R.string.complete_register_title,
                        leadingIcon = R.drawable.arrow_back_icon,
                        onLeadingClick = { navController.popBackStack() },
                    )
                )
            }

            Routes.FILTERS -> {
                topBarConfig(
                    TopBarConfig(
                        true,
                        title = R.string.filter_selector_title,
                        leadingIcon = R.drawable.arrow_back_icon,
                        onLeadingClick = { navController.popBackStack() },
                    )
                )
            }

            Routes.CHAT -> {
                val previousEntry = navController.previousBackStackEntry
                val targetName = previousEntry?.savedStateHandle?.get<String>("target_user_name") ?: "Chat"
                val targetPic = previousEntry?.savedStateHandle?.get<String>("target_user_pic") ?: ""

                topBarConfig(
                    TopBarConfig(
                        show = true,
                        leadingIcon = R.drawable.arrow_back_icon,
                        onLeadingClick = { navController.popBackStack() },
                        customContent = {
                            val dimensions = LocalDimensions.current

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(dimensions.standard)
                            ) {
                                ProfilePic(targetPic, dimensions.huge)
                                Text(
                                    text = targetName,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )
                )
            }

            Routes.ABOUT_US -> {
                topBarConfig(
                    TopBarConfig(
                        true,
                        title = R.string.about_us_title,
                        leadingIcon = R.drawable.arrow_back_icon,
                        onLeadingClick = { navController.popBackStack() },
                    )
                )
            }

            else -> topBarConfig(TopBarConfig(show = false))
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize(),
        //enterTransition = { fadeIn(animationSpec = tween(1000)) },
        //exitTransition = { fadeOut(animationSpec = tween(1000)) }
    ) {
        composable(Routes.PROFILE) {
            ProfileScreen(
                scaffoldPadding,
            )
        }
        composable(Routes.OTHER_PROFILE) {
            val targetUserId = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("target_user_id")

            ProfileScreen(
                scaffoldPadding,
                targetUserId = targetUserId
            )
        }
        composable(Routes.DISCOVER) { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle

            val filterCity by savedStateHandle.getStateFlow<String?>("filter_city", null)
                .collectAsState()
            val filterRooms by savedStateHandle.getStateFlow<Int?>("filter_rooms", null)
                .collectAsState()
            val filterBathrooms by savedStateHandle.getStateFlow<Int?>("filter_bathrooms", null)
                .collectAsState()
            val filterTags by savedStateHandle.getStateFlow<List<String>?>("filter_tags", null)
                .collectAsState()

            DiscoverScreen(
                scaffoldPadding = scaffoldPadding,
                onNavigateToProfile = { userId ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "target_user_id",
                        userId
                    )
                    navController.navigate(Routes.OTHER_PROFILE)
                },
                filterCity = filterCity,
                filterRooms = filterRooms,
                filterBathrooms = filterBathrooms,
                filterTags = filterTags
            )
        }
        composable(Routes.MATCHLIST) {
            MatchListScreen(
                scaffoldPadding = scaffoldPadding,
                onChatClick = { user ->
                    navController.currentBackStackEntry?.savedStateHandle?.apply {
                        set("target_user_id", user.id)
                        set("target_user_name", "${user.name} ${user.surname}, ${user.accommodation?.city}")
                        set("target_user_pic", user.profilePicUrl)
                    }
                    navController.navigate(Routes.CHAT)
                },
                onNavigateToProfile = { userId ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "target_user_id",
                        userId
                    )
                    navController.navigate(Routes.OTHER_PROFILE)
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                { navController.navigate(Routes.REGISTER) },
                {
                    navController.navigate(Routes.PROFILE) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegisterStep2 = { partialUser ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "temp_user",
                        partialUser
                    )
                    navController.navigate(Routes.REGISTER_STEP_2)
                },
                 onForgotPassword = { navController.navigate(Routes.RECOVER_PASSWORD) }
            )
        }
        composable(Routes.RECOVER_PASSWORD) {
            RecoverPasswordScreen(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onLogin = { navController.navigate(Routes.LOGIN) },
                onNextStep = { partialUser ->
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "temp_user",
                        partialUser
                    )
                    navController.navigate(Routes.REGISTER_STEP_2)
                }
            )
        }
        composable(Routes.REGISTER_STEP_2) {
            val partialUser = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<UserProfile>("temp_user")

            Box(Modifier.padding(scaffoldPadding)) {
                EditProfileScreen(
                    userData = partialUser,
                    isRegisterMode = true,
                    onGoToBack = { navController.popBackStack() },
                    onRegisterSuccess = { navController.navigate(Routes.PROFILE) }
                )
            }
        }
        composable(Routes.EDITPROFILE) {
            Box(Modifier.padding(scaffoldPadding)) {
                EditProfileScreen(
                    userData = null,
                    isRegisterMode = false,
                    onGoToBack = { navController.popBackStack() }
                )
            }
        }
        composable(Routes.FILTERS) {
            val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
            val initialCity = savedStateHandle?.get<String>("filter_city") ?: ""
            val initialRooms = savedStateHandle?.get<Int>("filter_rooms") ?: 1
            val initialBathrooms = savedStateHandle?.get<Int>("filter_bathrooms") ?: 1
            val initialTags = savedStateHandle?.get<List<String>>("filter_tags") ?: emptyList()

            Box(Modifier.padding(scaffoldPadding)) {
                FilterSelectionScreen(
                    initialCity = initialCity,
                    initialRooms = initialRooms,
                    initialBathrooms = initialBathrooms,
                    initialTags = initialTags,
                    onGoToBack = { navController.popBackStack() },
                    onSave = { filterState ->
                        savedStateHandle?.apply {
                            set("filter_city", filterState.city)
                            set("filter_rooms", filterState.rooms)
                            set("filter_bathrooms", filterState.bathrooms)
                            set("filter_tags", filterState.accommodationTags.map { it.name })
                        }
                        navController.popBackStack()
                    }
                )
            }
        }
        composable(Routes.CHAT) {
            val targetUserId = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<String>("target_user_id") ?: ""

            ChatScreen(
                scaffoldPadding = scaffoldPadding,
                targetUserId = targetUserId
            )
        }
        composable(Routes.ABOUT_US) {
            Box(Modifier.padding(scaffoldPadding)) {
                AboutUsScreen()
            }
        }
    }
}