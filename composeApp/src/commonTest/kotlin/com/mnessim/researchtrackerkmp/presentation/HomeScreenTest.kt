package com.mnessim.researchtrackerkmp.presentation

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import com.mnessim.researchtrackerkmp.domain.models.Term
import com.mnessim.researchtrackerkmp.domain.repositories.ITermsRepo
import com.mnessim.researchtrackerkmp.presentation.screens.homescreen.HomeScreen
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.Test

class HomeScreenTest {

    @OptIn(ExperimentalTestApi::class)
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
                HomeScreen(onNavigate = {}, onNotificationButton = {})
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

class FakeTermsRepo : ITermsRepo {
    private val terms = mutableListOf(
        Term(id = 1L, term = "Test Term", locked = false)
    )

    override fun getAllTerms(): List<Term> = terms.toList()
    override fun getTermById(id: Long): Term? = terms.find { it.id == id }
    override fun insertTerm(term: String, locked: Boolean) {
        val nextId = (terms.maxOfOrNull { it.id } ?: 0L) + 1
        terms.add(Term(id = nextId, term = term, locked = locked))
    }

    override fun updateTerm(term: Term) {
        val idx = terms.indexOfFirst { it.id == term.id }
        if (idx != -1) terms[idx] = term
    }

    override fun deleteTerm(id: Long) {
        terms.removeAll { it.id == id }
    }
}