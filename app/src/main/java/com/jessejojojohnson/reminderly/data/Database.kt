package com.jessejojojohnson.reminderly.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class Database {
    companion object {
        fun getDb(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "reminderly-db"
            ).build()
        }
    }
}

@Database(
    entities = [RedditAccessTokenEntity::class, ContentEntity::class],
    exportSchema = false,
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun redditAccessTokenDao(): RedditAccessTokenDao
    abstract fun contentDao(): ContentDao
}