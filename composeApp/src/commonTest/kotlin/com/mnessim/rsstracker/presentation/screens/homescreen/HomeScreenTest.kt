package com.mnessim.rsstracker.presentation.screens.homescreen

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.mnessim.rsstracker.domain.repositories.ITermsRepo
import com.mnessim.rsstracker.presentation.FakeTermsRepo
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class HomeScreenTest {

    @Test
    fun homeScreenTest() = runComposeUiTest {
        val testModule = module {
            single<ITermsRepo> { FakeTermsRepo() }
        }
        startKoin {
            modules(testModule)
        }
        try {
            setContent {
                HomeScreen(onNavigate = {}, onNotificationButton = {}, onNavigateToTiles = {})
            }
            // Basic UI elements with no terms
            onNodeWithTag("AddTermButton").assertExists().performClick()
            // AlertDialog UI
            onNodeWithTag("TermAlertDialog").assertExists()
            onNodeWithTag("DismissButton").assertExists()
            onNodeWithText("Test term").assertDoesNotExist()
            onNodeWithTag("TermTextField").assertExists().performTextInput("Test term")
            onNodeWithTag("SubmitButton").assertExists().performClick()
            // After term added
            onNodeWithText("Test term").assertExists()
            // Lock term and try to delete
            onNodeWithTag("ToggleLockButton").assertExists().performClick()
            onNodeWithTag("TermNotificationsButton").assertExists()
            onNodeWithTag("DeleteButton").assertExists().performClick()
            onNodeWithText("Test term").assertExists()
            // Unlock term and try to delete
            onNodeWithTag("ToggleLockButton").performClick()
            onNodeWithTag("DeleteButton").performClick()
            onNodeWithText("Test term").assertDoesNotExist()

        } finally {
            stopKoin()
        }
    }
}