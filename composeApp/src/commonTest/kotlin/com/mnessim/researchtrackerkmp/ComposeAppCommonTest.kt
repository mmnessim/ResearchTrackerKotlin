package com.mnessim.researchtrackerkmp

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ComposeAppCommonTest {

    @Test
    fun example() {
        assertEquals(3, 1 + 2)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun composableTest() = runComposeUiTest {
        setContent {
            App()
        }

        onNodeWithText("Research Tracker").assertExists().assertIsDisplayed()
        onNodeWithTag("AddTermButton").assertExists()
    }
}