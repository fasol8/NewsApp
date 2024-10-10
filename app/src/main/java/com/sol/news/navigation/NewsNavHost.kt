package com.sol.news.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sol.news.presentation.NewsListScreen
import com.sol.news.presentation.SavedNewsScreen
import com.sol.news.presentation.SearchNewsScreen

@Composable
fun NewsNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NewsScreen.NewsListScreen.route) {
        composable(NewsScreen.NewsListScreen.route) { NewsListScreen() }
        composable(NewsScreen.SavedNewsScreen.route) { SavedNewsScreen() }
        composable(NewsScreen.SearchNewsScreen.route) { SearchNewsScreen() }
    }
}