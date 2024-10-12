package com.sol.news.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.news.data.local.ArticleEntity
import com.sol.news.data.repository.NewsRepository
import com.sol.news.domain.model.Article
import com.sol.news.domain.model.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<List<Article>>(emptyList())
    val news: LiveData<List<Article>> = _news

    fun getNews(type: NewsType, query: String? = null, country: String = "us") {
        viewModelScope.launch {
            try {
                val articles = when (type) {
                    NewsType.BREAKING -> {
                        val response = repository.getTopHeadlines(country)
                        response.articles
                    }

                    NewsType.SAVED -> {
                        val savedArticles = repository.getSavedArticles()
                        savedArticles.map { entity ->
                            Article(
                                source = Source(entity.source.id, entity.source.name),
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

                    NewsType.SEARCH -> {
                        val response = repository.getSearchNews(query ?: "")
                        response.articles
                    }
                }
                _news.value = articles
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }

    fun saveArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            val newArticle =
                ArticleEntity(
                    sourceName = article.source.name ?: "",
                    author = article.author ?: "",
                    url = article.url ?: "",
                    urlToImage = article.urlToImage ?: "",
                    description = article.description ?: "",
                    publishedAt = article.publishedAt ?: "",
                    title = article.title ?: "",
                    content = article.content ?: ""
                )
            repository.saveArticle(newArticle)
        }
    }

    fun isArticleSaved(article: Article, callback: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val isSaved = repository.isArticleSaved(article.url)
            withContext(Dispatchers.Main) {
                callback(isSaved)
            }
        }
    }

    fun deleteArticleByUrl(articleUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteArticleByUrl(articleUrl)
        }
    }

}

enum class NewsType {
    BREAKING,
    SAVED,
    SEARCH
}