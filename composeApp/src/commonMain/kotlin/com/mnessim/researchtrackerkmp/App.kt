package com.mnessim.researchtrackerkmp

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import com.mnessim.researchtrackerkmp.presentation.core.AppBar
import com.mnessim.researchtrackerkmp.presentation.core.AppStartScheduler
import com.mnessim.researchtrackerkmp.presentation.core.ColorSchemeDialog
import com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen.DetailsScreen
import com.mnessim.researchtrackerkmp.presentation.screens.homescreen.HomeScreen
import com.mnessim.researchtrackerkmp.presentation.screens.navTilesScreen.NavTilesScreen
import com.mnessim.researchtrackerkmp.presentation.theme.darkScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastDarkColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastLightColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
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
    var colorScheme by remember { mutableStateOf(lightScheme) }
    var showColorSchemeDialog by remember { mutableStateOf(false) }

    val prefsRepo = koinInject<PreferencesRepo>()
    val manager = koinInject<NotificationManager>()


    LaunchedEffect(Unit) {
        NavigationEvents.navigateToDetails.collectLatest { id ->
            println("Collected navigateToDetails event: $id")
            if (id != null) {
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
                println("Route changed, resetting navigation event")
                NavigationEvents.triggerNavigateToDetails(null)
            }
        }
    }


    loadColorScheme(prefsRepo, { it -> colorScheme = it })

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
            colorScheme = colorScheme
        ) {

            if (showColorSchemeDialog) {
                ColorSchemeDialog(
                    activeScheme = colorScheme,
                    onDismiss = { showColorSchemeDialog = false },
                    onColorSchemeChange = { it ->
                        onColorSchemeChange(
                            prefsRepo, it, { it -> colorScheme = it }
                        )
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
                                    title = term.term,
                                    message = "Tap to see more",
                                    id = term.id
                                )
                            }
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
                            id = details.id
                        )
                    } // composable<Details>
                    composable<NavTilesRoute> {
                        NavTilesScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainer),
                            onHome = { navController.navigate(HomeRoute) }
                        )
                    }
                } // NavHost
            } // Box
        } // MaterialTheme
    } // Scaffold
} // App

/**
 * Update state and preferences when changing colorScheme
 *
 * @param repo Preferences repository for storing changes persistently
 * @param colorScheme Selected scheme as a String
 * @param onUpdate Callback to apply the new scheme
 */
fun onColorSchemeChange(
    repo: PreferencesRepo,
    colorScheme: String,
    onUpdate: (ColorScheme) -> Unit
) {
    val current = repo.getPrefByKey("colorScheme")
    if (current == null) {
        repo.insertPref("colorScheme", colorScheme)
    } else {
        repo.updatePref("colorScheme", colorScheme)
    }
    when (colorScheme) {
        "light" -> onUpdate(lightScheme)
        "dark" -> onUpdate(darkScheme)
        "lightContrast" -> onUpdate(highContrastLightColorScheme)
        "darkContrast" -> onUpdate(highContrastDarkColorScheme)
        else -> onUpdate(lightScheme)
    }
}

/**
 * Loads the saved color scheme from preferences and applies it
 *
 * @param repo Preferences repository for retrieving the color scheme
 * @param onUpdate Callback to apply the loaded color scheme
 */
fun loadColorScheme(
    repo: PreferencesRepo,
    onUpdate: (ColorScheme) -> Unit
) {
    val current = repo.getPrefByKey("colorScheme") ?: return
    when (current) {
        "light" -> onUpdate(lightScheme)
        "dark" -> onUpdate(darkScheme)
        "lightContrast" -> onUpdate(highContrastLightColorScheme)
        "darkContrast" -> onUpdate(highContrastDarkColorScheme)
        else -> onUpdate(lightScheme)
    }
}