package com.sol.news.data.repository

import com.sol.news.data.network.NewsApi
import com.sol.news.domain.model.NewsResponse
import javax.inject.Inject

class NewsRepository @Inject constructor(private val api: NewsApi) {

    suspend fun getTopHeadlines(country: String): NewsResponse {
        return api.getTopHeadlines(country)
    }

    suspend fun getSearchNews(query: String): NewsResponse {
        return api.getSearchNews(query)
    }
}