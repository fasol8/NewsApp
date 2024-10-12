package com.sol.news.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: ArticleEntity)

    @Delete
    suspend fun deleteArticle(article: ArticleEntity)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM articles WHERE url = :articleUrl)")
    suspend fun isArticleSaved(articleUrl: String): Boolean

    @Query("SELECT * FROM articles WHERE url = :articleUrl LIMIT 1")
    suspend fun getArticleByUrl(articleUrl: String): ArticleEntity
}