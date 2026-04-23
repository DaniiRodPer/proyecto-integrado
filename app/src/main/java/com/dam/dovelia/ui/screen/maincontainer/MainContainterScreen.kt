package com.dam.dovelia.ui.screen.maincontainer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dam.dovelia.R
import com.dam.dovelia.data.network.SessionManager
import com.dam.dovelia.ui.common.LocalDimensions
import com.dam.dovelia.ui.common.TopBarConfig
import com.dam.dovelia.ui.components.CustomizableTopBar
import com.dam.dovelia.ui.home.NavHostScreen
import com.dam.dovelia.ui.home.Routes

@Composable
fun MainContainerScreen(navController: NavHostController, sessionManager: SessionManager, startRoute: String, modifier: Modifier = Modifier) {
    var topBarConfig by remember { mutableStateOf(TopBarConfig(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val dimensions = LocalDimensions.current

    val showBottomBar = currentRoute in listOf(Routes.PROFILE, Routes.DISCOVER, Routes.MATCHLIST, Routes.OTHER_PROFILE)

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            AnimatedVisibility(
                visible = topBarConfig.show,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                CustomizableTopBar(topBarConfig)
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            dimensions.large,
                        )
                ) {
                    NavigationBar(
                        modifier = Modifier
                            .height(dimensions.extraHuge)
                            .clip(RoundedCornerShape(dimensions.extraBig)),
                        containerColor = MaterialTheme.colorScheme.surface,
                        windowInsets = WindowInsets(0, 0, 0, 0)
                    ) {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        val navItemColors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.background,
                            selectedTextColor = Color.White,
                            indicatorColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground
                        )

                        NavigationBarItem(
                            colors = navItemColors,
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.profile_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(dimensions.big)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.nav_profile_desc),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == Routes.PROFILE } == true,
                            onClick = {
                                navController.navigate(Routes.PROFILE) {
                                    popUpTo(navController.graph.id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        NavigationBarItem(
                            colors = navItemColors,
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.discover_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(dimensions.big)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.nav_discover_desc),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == Routes.DISCOVER } == true,
                            onClick = {
                                navController.navigate(Routes.DISCOVER) {
                                    popUpTo(navController.graph.id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                        NavigationBarItem(
                            colors = navItemColors,
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.match_icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(dimensions.big)
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.nav_match_desc),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 14.sp
                                    )
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == Routes.MATCHLIST } == true,
                            onClick = {
                                navController.navigate(Routes.MATCHLIST) {
                                    popUpTo(navController.graph.id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHostScreen(
            navController,
            scaffoldPadding = paddingValues,
            topBarConfig = { it -> topBarConfig = it },
            sessionManager = sessionManager,
            startDestination = startRoute
        )
    }
}