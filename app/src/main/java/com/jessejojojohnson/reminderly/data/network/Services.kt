package com.jessejojojohnson.reminderly.data.network

import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NetworkService

interface RedditService : NetworkService {

    @POST("access_token")
    fun getAccessToken(@Body request: RedditAccessTokenRequest): Call<RedditAccessTokenResponse>

    @GET("user/{username}/saved")
    fun getSavedContent(
        @Path("username") username: String
    ): Call<JsonObject>

    companion object {
        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            return loggingInterceptor
        }

        private val basicAuthInterceptor = object: Interceptor {
            private val credentials: String = Credentials.basic(
                "", "")
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val authenticatedRequest = request.newBuilder()
                    .header("Authorization", credentials).build()
                return chain.proceed(authenticatedRequest)
            }
        }

        private val bearerAuthInterceptor = Interceptor { chain ->
            val request = chain.request()
            val authenticatedRequest = request.newBuilder()
                .header(
                    "Authorization",
                    "bearer XXX"
                ).build()
            chain.proceed(authenticatedRequest)
        }

        private val userAgentInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("User-Agent", "Reminderly/0.1 by _wsgeorge")
                .build()
            chain.proceed(request)
        }

        fun getService() : RedditService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://oauth.reddit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                    .addInterceptor(userAgentInterceptor)
                    .addInterceptor(bearerAuthInterceptor)
                    .addInterceptor(getLoggingInterceptor())
                    .build())
                .build()
            return retrofit.create(RedditService::class.java)
        }

        fun getAuthService() : RedditAuthService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://www.reddit.com/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(userAgentInterceptor)
                        .addInterceptor(basicAuthInterceptor)
                        .addInterceptor(getLoggingInterceptor())
                        .build())
                .build()
            return retrofit.create(RedditAuthService::class.java)
        }
    }
}

interface RedditAuthService : RedditService

class HackerNewsService : NetworkService {

    fun getFavouriteComments(username: String): List<HackerNewsFavouritedContentResponse> {
        val url = "https://news.ycombinator.com/favorites?id=$username&comments=t"
        return emptyList()
    }

    fun getFavoriteSubmissions(username: String): List<HackerNewsFavouritedContentResponse> {
        val url = "https://news.ycombinator.com/favorites?id=$username"

        val doc = Jsoup.connect(url).get()
        val favoriteItems = doc.select("tr.athing")
        val favorites = mutableListOf<HackerNewsFavouritedContentResponse>()

        for (item in favoriteItems) {
            val rankElement = item.selectFirst("span.rank")
            val rank = rankElement?.text()

            val titleElement = item.selectFirst("span.titleline > a")
            val title = titleElement?.text()
            val link = titleElement?.attr("href")

            val submitterElement = item.nextElementSibling()?.selectFirst("a.hnuser")
            val submitter = submitterElement?.text()

            favorites.add(
                HackerNewsFavouritedContentResponse(
                    rank = rank ?: "",
                    title = title ?: "",
                    link = link ?: "",
                    submitter = submitter ?: ""
                )
            )
        }
        return favorites
    }

    companion object {
        fun getService() : HackerNewsService = HackerNewsService()
    }
}
