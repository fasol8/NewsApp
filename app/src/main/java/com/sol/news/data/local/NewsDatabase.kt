package com.sol.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleEntity::class], version = 2)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}