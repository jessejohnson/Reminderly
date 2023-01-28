package com.jessejojojohnson.reminderly.models

import com.jessejojojohnson.reminderly.R

data class ContentItem(
    val imageUrl: String,
    val title: String,
    val text: String,
    val source: ContentSource,
    val dateSaved: Long
)

sealed class ContentSource(val name: String, val iconResource: Int) {
    object HackerNews : ContentSource("HackerNews", R.drawable.ic_source_hn)
    object Twitter : ContentSource("Twitter", R.drawable.ic_source_twitter)
    object Reddit : ContentSource("Reddit", R.drawable.ic_source_reddit)
}