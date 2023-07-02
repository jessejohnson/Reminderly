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

    val db: AppDatabase by inject(AppDatabase::class.java)
    val hackerNewsService: HackerNewsService by inject(HackerNewsService::class.java)

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

    override fun doWork(): Result {
        val requestBody = RedditAccessTokenRequest(
            grant_type = "password",
            username = "",
            password = ""
        )
        val response = redditAuthService.getAccessToken(requestBody)
            .execute()

        Log.d("WM", Gson().toJson(requestBody))
        Log.d("WM", response.message() + " " + response.code())
        Log.d("WM", response.errorBody().toString())

        response.body()?.accessToken?.let {
            Log.d("WM", "Token is $it")
            db.redditAccessTokenDao().insert(
                RedditAccessTokenEntity(token = it)
            )
            return Result.success()
        }
        return Result.failure()
    }
}

class GetRedditContentWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    val redditService: RedditService by inject(RedditService::class.java)
    val db: AppDatabase by inject(AppDatabase::class.java)
    val gson = Gson()

    override fun doWork(): Result {

        val response = redditService.getSavedContent("_wsgeorge").execute()
        Log.d("WM", response.message() + " " + response.code())

        response.body()?.let { jsonResponse ->
            //Log.d("WM", "Response is $it")
            val array = jsonResponse["data"].asJsonObject["children"].asJsonArray
            array.forEach { jsonElement ->
                //Log.d("WM", "Response is ${jsonElement.asJsonObject["data"]}")
                val item: RedditSavedContentResponse = gson.fromJson(
                    jsonElement.asJsonObject["data"].toString(),
                    RedditSavedContentResponse::class.java
                )
                val entity = ContentEntity(
                    id = UUID.randomUUID().toString(),
                    title = when {
                        !item.title.isNullOrEmpty() -> { item.title }
                        !item.linkTitle.isNullOrEmpty() -> { item.linkTitle }
                        else -> { "" }
                    },
                    text = when {
                        !item.selfText.isNullOrEmpty() -> { item.selfText}
                        !item.body.isNullOrEmpty() -> { item.body }
                        else -> ""
                    },
                    imageUrl = item.thumbnail ?: "",
                    link = "www.reddit.com${item.permalink}",
                    source = Source.REDDIT.name,
                    dateTimeSaved = System.currentTimeMillis()
                )
                db.contentDao().insert(entity)
            }
            return Result.success()
        }

        return Result.failure()
    }
}