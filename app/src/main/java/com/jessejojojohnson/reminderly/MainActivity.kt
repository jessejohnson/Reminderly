package com.jessejojojohnson.reminderly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jessejojojohnson.reminderly.data.GetHackerNewsContentWorker
import com.jessejojojohnson.reminderly.ui.screens.MainScreen
import com.jessejojojohnson.reminderly.ui.theme.ReminderlyTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val workManager by inject<WorkManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReminderlyTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(modifier = Modifier.fillMaxSize())
                    LaunchedEffect(key1 = Unit) {
                        workManager.enqueue(
                            OneTimeWorkRequestBuilder<GetHackerNewsContentWorker>().build()
                        )
                    }
                }
            }
        }
    }
}