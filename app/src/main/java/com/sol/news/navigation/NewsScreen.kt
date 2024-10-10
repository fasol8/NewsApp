package com.sol.news.navigation

sealed class NewsScreen(val route: String) {
    data object NewsSplashScreen:NewsScreen("newsSplashscreen")
    data object NewsListScreen : NewsScreen("newsListScreen")
    data object SavedNewsScreen : NewsScreen("savedNewsScreen")
    data object SearchNewsScreen : NewsScreen("searchNewsScreen")
}