package com.dam.proydrp.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dam.proydrp.R
import com.dam.proydrp.ui.common.TopBarConfig
import com.dam.proydrp.ui.components.CustomAlertDialog
import com.dam.proydrp.ui.components.text.Title
import com.dam.proydrp.ui.screen.matchlist.MatchListScreen
import com.dam.proydrp.ui.screen.discover.DiscoverScreen
import com.dam.proydrp.ui.screen.editprofile.EditProfileScreen
import com.dam.proydrp.ui.screen.filterselection.FilterSelectionScreen
import com.dam.proydrp.ui.screen.login.LoginScreen
import com.dam.proydrp.ui.screen.profile.ProfileScreen
import com.dam.proydrp.ui.screen.register.RegisterScreen

object Routes {
    const val PROFILE = "profile"
    const val DISCOVER = "discover"
    const val MATCHLIST = "matchlist"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val EDITPROFILE = "editprofile"
    const val FILTERS = "filters"

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
    topBarConfig: (TopBarConfig) -> Unit

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
                                    activeDialog = null
                                    navController.navigate(Routes.LOGIN)
                                },
                                onDismiss = { activeDialog = null }
                            )
                        },
                        trailingIcon = R.drawable.edit_icon,
                        onTrailingClick = { navController.navigate(Routes.EDITPROFILE) }
                    ))
            }

            Routes.DISCOVER -> {
                topBarConfig(
                    TopBarConfig(
                        true,
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

            Routes.LOGIN, Routes.REGISTER -> {
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

            else -> topBarConfig(TopBarConfig(show = false))
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.MATCHLIST,
        modifier = Modifier.fillMaxSize(),
        //enterTransition = { fadeIn(animationSpec = tween(1000)) },
        //exitTransition = { fadeOut(animationSpec = tween(1000)) }
    ) {
        composable(Routes.PROFILE) {
            ProfileScreen(
                scaffoldPadding,
            )
        }
        composable(Routes.DISCOVER) {
            DiscoverScreen(
                scaffoldPadding,
            )
        }
        composable(Routes.MATCHLIST) {
            MatchListScreen(
                scaffoldPadding,
                {},
                {}
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                { navController.navigate(Routes.REGISTER) },
                { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                { navController.navigate(Routes.LOGIN) })
        }
        composable(Routes.EDITPROFILE) {
            Box(Modifier.padding(scaffoldPadding)) {
                EditProfileScreen({ navController.popBackStack() })
            }
        }
        composable(Routes.FILTERS) {
            Box(Modifier.padding(scaffoldPadding)) {
                FilterSelectionScreen(
                    { navController.popBackStack() })
            }
        }
    }
}