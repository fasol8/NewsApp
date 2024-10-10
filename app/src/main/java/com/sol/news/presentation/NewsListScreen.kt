package com.sol.news.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sol.news.domain.model.Article

@Composable
fun NewsListScreen(newsViewModel: NewsViewModel = hiltViewModel()) {
    val news by newsViewModel.news.observeAsState(emptyList())

    LaunchedEffect(true) {
        newsViewModel.getNews()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn {
                items(news.size) { index ->
                    val new = news[index]
                    CardNewItem(new)
                }
            }
        }
    }
}

@Composable
fun CardNewItem(new: Article) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = new.title ?: "", style = MaterialTheme.typography.titleLarge)
            Text(text = new.author ?: "", style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun SavedNewsScreen(modifier: Modifier = Modifier) {
    Text(text = "Saved News", modifier = modifier.padding(16.dp))
}

@Composable
fun SearchNewsScreen(modifier: Modifier = Modifier) {
    Text(text = "Search News", modifier = modifier.padding(16.dp))
}