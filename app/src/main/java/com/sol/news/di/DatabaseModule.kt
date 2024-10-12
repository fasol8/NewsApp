package com.sol.news.di

import android.content.Context
import androidx.room.Room
import com.sol.news.data.local.ArticleDao
import com.sol.news.data.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return Room.databaseBuilder(
            appContext,
            NewsDatabase::class.java,
            "news_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideArticleDao(database: NewsDatabase): ArticleDao {
        return database.articleDao()
    }
}