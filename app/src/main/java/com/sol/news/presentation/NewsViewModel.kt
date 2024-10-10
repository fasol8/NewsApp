package com.sol.news.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sol.news.data.repository.NewsRepository
import com.sol.news.domain.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(private val repository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<List<Article>>(emptyList())
    val news: LiveData<List<Article>> = _news

    fun getNews(type: NewsType, query: String? = null, country: String = "us") {
        viewModelScope.launch {
            try {
                val response = when (type) {
                    NewsType.BREAKING -> repository.getTopHeadlines(country)
                    NewsType.SEARCH -> repository.getSearchNews(query ?: "")
                }
                _news.value = response.articles
            } catch (e: Exception) {
                Log.i("Error", e.message.toString())
            }
        }
    }
}

enum class NewsType {
    BREAKING,
    SEARCH
}