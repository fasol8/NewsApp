package com.sol.news.navigation

import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.sol.news.R

@Composable
fun MainBottomBar() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) {
        NewsNavHost(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_newspaper),
                    contentDescription = "Breaking News"
                )
            },
            label = { Text("Breaking News") },
            selected = navController.currentBackStackEntry?.destination?.route == NewsScreen.NewsListScreen.route,
            onClick = { navController.navigate(NewsScreen.NewsListScreen.route) }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark),
                    contentDescription = "Saved News"
                )
            },
            label = { Text("Saved News") },
            selected = navController.currentBackStackEntry?.destination?.route == NewsScreen.SavedNewsScreen.route,
            onClick = { navController.navigate(NewsScreen.SavedNewsScreen.route) }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search News"
                )
            },
            label = { Text("Search News") },
            selected = navController.currentBackStackEntry?.destination?.route == NewsScreen.SearchNewsScreen.route,
            onClick = { navController.navigate(NewsScreen.SearchNewsScreen.route) }
        )
    }
}