package com.mnessim.researchtrackerkmp

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mnessim.researchtrackerkmp.presentation.screens.detailsscreen.DetailsScreen
import com.mnessim.researchtrackerkmp.presentation.screens.homescreen.HomeScreen
import com.mnessim.researchtrackerkmp.presentation.theme.lightScheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val canPop = navBackStackEntry?.destination?.route != Home::class.qualifiedName

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
                }
            )
        }
    ) { innerPadding ->
        MaterialTheme(
            colorScheme = lightScheme
        ) {
            NavHost(
                navController = navController,
                startDestination = Home,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None }
            ) {
                composable<Home> {
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        onNavigate = { id -> navController.navigate(Details(id)) })
                }
                composable<Details> { backStackEntry ->
                    val details: Details = backStackEntry.toRoute<Details>()
                    DetailsScreen(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        onBack = {
                            navController.popBackStack()
                        },
                        id = details.id
                    )
                }
            }
        }
    }


}


