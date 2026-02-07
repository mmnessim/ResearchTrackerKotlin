package com.mnessim.rsstracker

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mnessim.rsstracker.domain.services.ColorSchemeService
import com.mnessim.rsstracker.presentation.core.AppBar
import com.mnessim.rsstracker.presentation.core.AppStartScheduler
import com.mnessim.rsstracker.presentation.core.ColorSchemeDialog
import com.mnessim.rsstracker.presentation.screens.aboutScreen.AboutScreen
import com.mnessim.rsstracker.presentation.screens.detailsscreen.DetailsScreen
import com.mnessim.rsstracker.presentation.screens.homescreen.HomeScreen
import com.mnessim.rsstracker.presentation.screens.navTilesScreen.NavTilesScreen
import com.mnessim.rsstracker.presentation.screens.optionsScreen.OptionsScreen
import com.mnessim.rsstracker.presentation.screens.savedArticlesScreen.SavedArticlesScreen
import com.mnessim.rsstracker.presentation.theme.AppTypography
import com.mnessim.rsstracker.utils.notifications.NotificationManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UNUSED_VARIABLE")
@Preview
fun App(startDestination: AppRoute = NavTilesRoute) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canPop = navBackStackEntry?.destination?.route != NavTilesRoute::class.qualifiedName
    var showColorSchemeDialog by remember { mutableStateOf(false) }

    val colorService = koinInject<ColorSchemeService>()
    val colorScheme by colorService.scheme.collectAsState()

    val manager = koinInject<NotificationManager>()

    LaunchedEffect(Unit) {
        NavigationEvents.navigateToDetails.collectLatest { id ->
            println("Collected navigateToDetails event: $id")
            if (id != null) {
                if (id == -1L) {
                    navController.navigate(HomeRoute) {
                        launchSingleTop = true
                        restoreState = true
                    }
                } else {
                    val targetRoute = DetailsRoute(id)
                    navController.navigate(DetailsRoute(id)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    // Wait for the route to actually change
                    snapshotFlow { navController.currentBackStackEntry?.destination?.route }
                        .dropWhile { newRoute ->
                            println("Waiting for route change: $newRoute")
                            newRoute != targetRoute.toString()
                        }
                        .first()
                }
                println("Route changed, resetting navigation event")
                NavigationEvents.triggerNavigateToDetails(null)
            }
        }
    } // LaunchedEffect

    Scaffold(
        topBar = {
            AppBar(
                colorScheme = colorScheme,
                canPop = canPop,
                onNavigate = { navController.popBackStack() },
                onChangeColorScheme = { showColorSchemeDialog = true },
                onNotificationButton = { manager.showNotification("Test", "test", 1) }
            ) // AppBar
        } // topBar =
    ) { innerPadding ->
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography
        ) {

            if (showColorSchemeDialog) {
                ColorSchemeDialog(
                    activeScheme = colorScheme,
                    onDismiss = { showColorSchemeDialog = false },
                    onColorSchemeChange = {
                        colorService.setScheme(it)
                    },
                ) // ColorSchemeDialog
            } // if (showColorSchemeDialog)

            AppStartScheduler()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainer)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    enterTransition = { fadeIn(animationSpec = tween(durationMillis = 180)) },
                    exitTransition = { fadeOut(animationSpec = tween(durationMillis = 180)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(durationMillis = 140)) },
                    popExitTransition = { fadeOut(animationSpec = tween(durationMillis = 140)) }
                ) {
                    composable<HomeRoute> {
                        HomeScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            onNavigate = { id -> navController.navigate(DetailsRoute(id)) },
                            onNavigateToTiles = { navController.navigate(NavTilesRoute) },
                            onNotificationButton = { term ->
                                manager.showNotification(
                                    "New results for ${term.term.replaceFirstChar { it.uppercase() }}",
                                    message = "Tap to see more",
                                    id = term.id
                                )
                            },
                        )
                    } // composable<Home>
                    composable<DetailsRoute> { backStackEntry ->
                        val details: DetailsRoute = backStackEntry.toRoute<DetailsRoute>()
                        DetailsScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            onBack = {
                                navController.navigate(HomeRoute)
                            },
                            id = details.id,
                            onError = {
                                navController.navigate(HomeRoute)
                            }
                        )
                    } // composable<Details>
                    composable<NavTilesRoute> {
                        NavTilesScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            onHome = { navController.navigate(HomeRoute) },
                            onOptions = { navController.navigate(OptionsRoute) },
                            onAbout = { navController.navigate(AboutRoute) },
                            onSavedArticles = { navController.navigate(SavedArticlesRoute) }
                        )
                    } // composable<NavTilesRoute>
                    composable<OptionsRoute> {
                        OptionsScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        )
                    } // composable<OptionsRoute>
                    composable<AboutRoute> {
                        AboutScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        )
                    } // composable<AboutRoute>
                    composable<SavedArticlesRoute> {
                        SavedArticlesScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                        )
                    } // composable<SavedArticlesRoute>
                } // NavHost
            } // Box
        } // MaterialTheme
    } // Scaffold
} // App
