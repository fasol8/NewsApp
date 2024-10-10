package com.sol.news.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sol.news.presentation.NewsListScreen
import com.sol.news.presentation.NewsType
import com.sol.news.presentation.SavedNewsScreen
import com.sol.news.presentation.SplashScreen

@Composable
fun NewsNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NewsScreen.NewsSplashScreen.route) {
        composable(NewsScreen.NewsSplashScreen.route) { SplashScreen(navController) }
        composable(NewsScreen.NewsListScreen.route) { NewsListScreen(newsType = NewsType.BREAKING) }
        composable(NewsScreen.SavedNewsScreen.route) { SavedNewsScreen() }
        composable(NewsScreen.SearchNewsScreen.route) {
            val query = "apple"
            NewsListScreen(newsType = NewsType.SEARCH, searchQuery = query)
        }
    }
}