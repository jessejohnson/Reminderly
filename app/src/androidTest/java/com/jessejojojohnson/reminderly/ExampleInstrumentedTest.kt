package com.jessejojojohnson.reminderly

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jessejojojohnson.reminderly.ui.models.ContentItem
import com.jessejojojohnson.reminderly.ui.models.ContentSource
import com.jessejojojohnson.reminderly.ui.components.ContentCard
import com.jessejojojohnson.reminderly.ui.screens.MainScreen
import com.jessejojojohnson.reminderly.ui.theme.ReminderlyTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.jessejojojohnson.reminderly", appContext.packageName)
    }
}

class FirstComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testContentCardWithoutContentText() {
        val missingText = "Hello, world! Let's pretend this is content I saved on the Interwebs!"
        val item = ContentItem(
            imageUrl = "",
            title = "My First Reminder!",
            text = "",
            dateSaved = System.currentTimeMillis(),
            source = ContentSource.HackerNews
        )
        // start app
        composeTestRule.setContent {
            ReminderlyTheme {
                //MainScreen(modifier = Modifier.fillMaxSize())
                ContentCard(item = item)
            }
        }

        composeTestRule.onRoot(useUnmergedTree = false).printToLog("ContentCard")
        composeTestRule.onNodeWithText(item.title).assertExists()
        composeTestRule.onNodeWithText(missingText).assertDoesNotExist()
    }

    @Test
    fun test2() {
       // val viewModel: MainScreenViewModel = mock()
        composeTestRule.setContent {
            ReminderlyTheme {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}