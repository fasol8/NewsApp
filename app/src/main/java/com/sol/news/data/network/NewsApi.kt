package com.sol.news.data.network

import com.sol.news.BuildConfig
import com.sol.news.domain.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String="us",
        @Query("apiKey") apiKey:String= BuildConfig.NEWS_API_KEY
    ):NewsResponse
}