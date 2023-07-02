package com.jessejojojohnson.reminderly

import android.app.Application
import androidx.work.WorkManager
import com.jessejojojohnson.reminderly.data.Database
import com.jessejojojohnson.reminderly.data.network.HackerNewsService
import com.jessejojojohnson.reminderly.data.network.RedditService
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class ReminderlyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ReminderlyApp)
            modules(networkModule, databaseModule)
        }
    }
}

val networkModule = module {
    single { RedditService.getService() }
    single { RedditService.getAuthService() }
    single { HackerNewsService.getService() }
    single { WorkManager.getInstance(androidContext()) }
}

val databaseModule = module {
    single { Database.getDb(androidContext()) }
}