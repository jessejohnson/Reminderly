package com.jessejojojohnson.reminderly.data

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.jessejojojohnson.reminderly.data.network.HackerNewsService
import com.jessejojojohnson.reminderly.data.network.RedditAccessTokenRequest
import com.jessejojojohnson.reminderly.data.network.RedditAuthService
import com.jessejojojohnson.reminderly.data.network.RedditSavedContentResponse
import com.jessejojojohnson.reminderly.data.network.RedditService
import com.jessejojojohnson.reminderly.randomId
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

class GetHackerNewsContentWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    private val db: AppDatabase by inject(AppDatabase::class.java)
    private val hackerNewsService: HackerNewsService by inject(HackerNewsService::class.java)

    override fun doWork(): Result {
        val list = hackerNewsService.getFavoriteSubmissions("wsgeorge")
        if (list.isNotEmpty()) db.contentDao().deleteAll()
        list.forEach {
            val entity = ContentEntity(
                id = randomId(),
                title = it.title,
                text = it.submitter,
                link = it.link,
                imageUrl = "",
                source = Source.HACKER_NEWS.name,
                dateTimeSaved = System.currentTimeMillis()
            )
            db.contentDao().insert(entity)
        }
        return Result.success()
    }
}

class GetRedditAccessTokenWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    val db: AppDatabase by inject(AppDatabase::class.java)
    val redditAuthService: RedditAuthService by inject(RedditAuthService::class.java)

    override fun doWork(): Result = Result.failure()

}

class GetRedditContentWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    val redditService: RedditService by inject(RedditService::class.java)
    val db: AppDatabase by inject(AppDatabase::class.java)
    val gson = Gson()

    override fun doWork(): Result = Result.failure()

}