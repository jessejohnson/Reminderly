package com.jessejojojohnson.reminderly.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity
data class ContentEntity(
    @PrimaryKey val id: String,
    val title: String,
    val text: String,
    val imageUrl: String,
    val link: String,
    val source: String,
    val dateTimeSaved: Long
)

enum class Source{
    TWITTER,
    REDDIT,
    HACKER_NEWS
}

@Entity
data class RedditAccessTokenEntity(
    @PrimaryKey val id: String = "wTTdQRFBobEpW",
    val token: String
)

@Dao
interface ContentDao {

    @Query("SELECT * from ContentEntity")
    fun getAll() : Flow<List<ContentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg content: ContentEntity)

    @Delete
    fun delete(vararg stories: ContentEntity)

    @Query("DELETE FROM ContentEntity")
    fun deleteAll()
}

@Dao
interface RedditAccessTokenDao {

    @Query("SELECT * from RedditAccessTokenEntity")
    fun getAll() : Flow<List<RedditAccessTokenEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg stories: RedditAccessTokenEntity)

    @Update
    fun update(vararg stories: RedditAccessTokenEntity)

    @Delete
    fun delete(vararg stories: RedditAccessTokenEntity)
}