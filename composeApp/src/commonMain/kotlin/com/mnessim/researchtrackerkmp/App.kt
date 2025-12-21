package com.mnessim.researchtrackerkmp

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.InvertColors
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen.DetailsScreen
import com.mnessim.researchtrackerkmp.presentation.screens.homescreen.HomeScreen
import com.mnessim.researchtrackerkmp.presentation.theme.darkScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastDarkColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.highContrastLightColorScheme
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canPop = navBackStackEntry?.destination?.route != Home::class.qualifiedName
    var colorScheme by remember { mutableStateOf(lightScheme) }
    var showColorSchemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Research Tracker") },
                navigationIcon = if (canPop) {
                    {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                } else {
                    {}
                }, // navigationIcon =
                actions = {
                    Surface(
                        modifier = Modifier
                            .padding(horizontal = 18.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onTap = {
                                        showColorSchemeDialog = true
                                    },
                                    onLongPress = {
                                        showColorSchemeDialog = true
                                    }
                                )
                            },
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.InvertColors,
                            contentDescription = "Toggle Dark Mode"
                        ) // Icon
                    } // Surface
                } // actions =
            ) // TopAppBar
        } // topbar =
    ) { innerPadding ->
        MaterialTheme(
            colorScheme = colorScheme
        ) {

            if (showColorSchemeDialog) {
                AlertDialog(
                    onDismissRequest = { showColorSchemeDialog = false },
                    title = { Text(text = "Choose Color Scheme") },
                    text = { Text("Select an option") },
                    confirmButton = {

                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            TextButton(onClick = { colorScheme = lightScheme }) {
                                Text("Light")
                            }
                            TextButton(onClick = { colorScheme = darkScheme }) {
                                Text("Dark")
                            }
                        }
                    },
                    dismissButton = {
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            TextButton(onClick = { colorScheme = highContrastLightColorScheme }) {
                                Text("Light Contrast")
                            }
                            TextButton(onClick = { colorScheme = highContrastDarkColorScheme }) {
                                Text("Dark Contrast")
                            }
                        }
                    }
                ) // AlertDialog
            }


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    enterTransition = { slideInHorizontally { it } + fadeIn() },
                    exitTransition = { slideOutHorizontally { -it } + fadeOut() },
                    popEnterTransition = { slideInHorizontally { -it } + fadeIn() },
                    popExitTransition = { slideOutHorizontally { it } + fadeOut() }
                ) {
                    composable<Home> {
                        HomeScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            onNavigate = { id -> navController.navigate(Details(id)) })
                    } // composable<Home>
                    composable<Details> { backStackEntry ->
                        val details: Details = backStackEntry.toRoute<Details>()
                        DetailsScreen(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            onBack = {
                                navController.popBackStack()
                            },
                            id = details.id
                        )
                    } // composable<Details>
                } // NavHost
            } // Box
        } // MaterialTheme
    } // Scaffold
}


