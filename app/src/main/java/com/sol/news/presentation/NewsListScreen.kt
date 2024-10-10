package com.sol.news.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.sol.news.domain.model.Article

@Composable
fun NewsListScreen(newsViewModel: NewsViewModel = hiltViewModel()) {
    val news by newsViewModel.news.observeAsState(emptyList())
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    LaunchedEffect(true) {
        newsViewModel.getNews()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Column {
            Spacer(modifier = Modifier.height(32.dp))
            LazyColumn {
                items(news.size) { index ->
                    val article = news[index]
                    CardArticleItem(article) {
                        selectedArticle = article
                    }
                }
            }
        }

        selectedArticle?.let { article ->
            ArticleDialog(article) {
                selectedArticle = null // Cierra el diálogo
            }
        }
    }
}

@Composable
fun CardArticleItem(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        onClick = { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Row {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "Image Article",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(100.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = article.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3
                )
                Text(
                    text = article.author ?: "Unknown",
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ArticleDialog(article: Article, onDismiss: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = article.title ?: "Article")
        },
        text = {
            Column {
                Text(text = "Author: ${article.author ?: "Unknown"}")
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = article.publishedAt ?: "????-??-??")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = article.description ?: "No description available")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Content: ${article.content ?: "No content available"}")

            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        dismissButton = {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                context.startActivity(intent)
            }) {
                Text(text = "Open Article")
            }
        }
    )
}

@Composable
fun SavedNewsScreen(modifier: Modifier = Modifier) {
    Text(text = "Saved News", modifier = modifier.padding(16.dp))
}

@Composable
fun SearchNewsScreen(modifier: Modifier = Modifier) {
    Text(text = "Search News", modifier = modifier.padding(16.dp))
}