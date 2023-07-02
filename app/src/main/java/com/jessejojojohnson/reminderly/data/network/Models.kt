package com.jessejojojohnson.reminderly.data.network

import com.google.gson.annotations.SerializedName

data class HackerNewsFavouritedContentResponse(
    val rank: String,
    val title: String,
    val link: String,
    val submitter: String
)

data class RedditAccessTokenRequest(
    val grant_type: String,
    val username: String,
    val password: String
)

data class RedditAccessTokenResponse(
    val accessToken: String,
    val expiresIn: Int
)

data class RedditSavedContentResponse(
    @SerializedName("link_title")
    val linkTitle: String?,
    val body: String?,
    val permalink: String?, //linkUrl?
    val title: String?,
    @SerializedName("selftext")
    val selfText: String?,
    val url: String?,
    val thumbnail: String?
)
