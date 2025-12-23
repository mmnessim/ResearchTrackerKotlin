package com.mnessim.researchtrackerkmp

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mnessim.researchtrackerkmp.domain.repositories.PreferencesRepo
import com.mnessim.researchtrackerkmp.presentation.core.AppBar
import com.mnessim.researchtrackerkmp.presentation.core.ColorSchemeDialog
import com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen.DetailsScreen
import com.mnessim.researchtrackerkmp.presentation.screens.homescreen.HomeScreen
import com.mnessim.researchtrackerkmp.presentation.theme.darkScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastDarkColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastLightColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import com.mnessim.researchtrackerkmp.utils.notifications.NotificationManager
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UNUSED_VARIABLE")
@Preview
fun App(startDestination: AppRoute = HomeRoute) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canPop = navBackStackEntry?.destination?.route != HomeRoute::class.qualifiedName
    var colorScheme by remember { mutableStateOf(lightScheme) }
    var showColorSchemeDialog by remember { mutableStateOf(false) }

    val prefsRepo = koinInject<PreferencesRepo>()
    val manager = koinInject<NotificationManager>()

    // Ensures proper iOS navigation from notifications
    if (isIos) {
        LaunchedEffect(Unit) {
            NavigationEvents.navigateToDetails.collectLatest { id ->
                if (id != null) {
                    navController.navigate(DetailsRoute(id))
                    NavigationEvents.triggerNavigateToDetails(null)
                }
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
                onNotificationButton = { manager.showNotification("Test", "test") }
            ) // AppBar
        } // topBar =
    ) { innerPadding ->
        MaterialTheme(
            colorScheme = colorScheme
        ) {

            if (showColorSchemeDialog) {
                ColorSchemeDialog(
                    onDismiss = { showColorSchemeDialog = false },
                    onColorSchemeChange = { it ->
                        onColorSchemeChange(
                            prefsRepo, it, { it -> colorScheme = it }
                        )
                    }
                ) // ColorSchemeDialog
            } // if (showColorSchemeDialog)


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    enterTransition = { slideInHorizontally { it } + fadeIn() },
                    exitTransition = { slideOutHorizontally { -it } + fadeOut() },
                    popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
                    popExitTransition = { slideOutHorizontally { it } + fadeOut() }
                ) {
                    composable<HomeRoute> {
                        HomeScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            onNavigate = { id -> navController.navigate(DetailsRoute(id)) })
                    } // composable<Home>
                    composable<DetailsRoute> { backStackEntry ->
                        val details: DetailsRoute = backStackEntry.toRoute<DetailsRoute>()
                        DetailsScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            onBack = {
                                navController.navigate(HomeRoute)
                            },
                            id = details.id
                        )
                    } // composable<Details>
                } // NavHost
            } // Box
        } // MaterialTheme
    } // Scaffold
} // App

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