package com.sol.news.data.repository

import com.sol.news.data.local.ArticleDao
import com.sol.news.data.local.ArticleEntity
import com.sol.news.data.network.NewsApi
import com.sol.news.domain.model.Article
import com.sol.news.domain.model.NewsResponse
import com.sol.news.domain.model.Source
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApi,
    private val articleDao: ArticleDao
) {

    suspend fun getTopHeadlines(country: String): NewsResponse {
        return api.getTopHeadlines(country)
    }

    suspend fun getSearchNews(query: String): NewsResponse {
        return api.getSearchNews(query)
    }

    suspend fun saveArticle(article: ArticleEntity) {
        articleDao.insertArticle(article)
    }

    suspend fun getSavedArticles(): List<Article> {
        return articleDao.getAllArticles().map { entity ->
            Article(
                source = Source(entity.sourceName, entity.sourceName),
                author = entity.author,
                title = entity.title,
                description = entity.description,
                url = entity.url,
                urlToImage = entity.urlToImage,
                publishedAt = entity.publishedAt,
                content = entity.content
            )
        }
    }

    suspend fun isArticleSaved(articleUrl: String): Boolean {
        return articleDao.isArticleSaved(articleUrl)
    }

    suspend fun deleteArticleByUrl(articleUrl: String) {
        val article = articleDao.getArticleByUrl(articleUrl)
        if (article != null) {
            articleDao.deleteArticle(article)
        }
    }
}