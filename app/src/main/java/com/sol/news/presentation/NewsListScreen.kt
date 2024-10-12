package com.sol.news.presentation

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.sol.news.R
import com.sol.news.domain.model.Article
import com.sol.news.utils.formatDate

@Composable
fun NewsListScreen(
    newsType: NewsType,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val news by newsViewModel.news.observeAsState(emptyList())
    var selectedArticle by remember { mutableStateOf<Article?>(null) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(newsType) {
        newsViewModel.getNews(newsType)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
    ) {
        Column {
            if (newsType == NewsType.SEARCH) {
                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = {
                        newsViewModel.getNews(newsType, query)
                    }
                )
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }
            if (news.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No news", style = MaterialTheme.typography.titleLarge)
                }
            } else {
                LazyColumn {
                    items(news.size) { index ->
                        val article = news[index]
                        if (article.title != "[Removed]") {
                            CardArticleItem(article, newsViewModel) {
                                selectedArticle = article
                            }
                        }
                    }
                }
            }
        }

        selectedArticle?.let { article ->
            ArticleDialog(article, newsViewModel) {
                selectedArticle = null // Cierra el diálogo
            }
        }
    }
}

@Composable
fun CardArticleItem(
    article: Article,
    newsViewModel: NewsViewModel,
    onClick: () -> Unit
) {
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(article) {
        newsViewModel.isArticleSaved(article) { saved ->
            isSaved = saved
        }
    }

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
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = article.title ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = article.author ?: "Unknown",
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = {
                        if (isSaved) {
                            newsViewModel.deleteArticleByUrl(article.url)
                            isSaved = false
                        } else {
                            newsViewModel.saveArticle(article)
                            isSaved = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(if (isSaved) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_border),
                            contentDescription = "bookmark",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleDialog(article: Article, newsViewModel: NewsViewModel, onDismiss: () -> Unit) {
    val context = LocalContext.current
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(article) {
        newsViewModel.isArticleSaved(article) { saved ->
            isSaved = saved
        }
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = article.title ?: "Article")
        },
        text = {
            Column {
                Row {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Author: ${article.author ?: "Unknown"}")
                        Spacer(modifier = Modifier.height(4.dp))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            Text(text = formatDate(article.publishedAt) ?: "????-??-??")
                        }
                    }
                    IconButton(onClick = {
                        if (isSaved) {
                            newsViewModel.deleteArticleByUrl(article.url)
                            isSaved = false
                        } else {
                            newsViewModel.saveArticle(article)
                            isSaved = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(if (isSaved) R.drawable.ic_bookmark_fill else R.drawable.ic_bookmark_border),
                            contentDescription = "bookmark",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    var active by remember { mutableStateOf(false) }

    val colors1 = SearchBarDefaults.colors()
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = { onQueryChange(it) },
                onSearch = {
                    onSearch(query)
                    active = false // Cierra el SearchBar después de buscar
                },
                expanded = active,
                onExpandedChange = { active = it },
                enabled = true,
                placeholder = { Text("Search news...") },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_search),
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = {
                            onQueryChange("")
                            active = false
                            onSearch("")
                        }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                colors = colors1.inputFieldColors,
                interactionSource = null,
            )
        },
        expanded = active,
        onExpandedChange = { active = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = SearchBarDefaults.inputFieldShape,
        colors = colors1,
        tonalElevation = SearchBarDefaults.TonalElevation,
        shadowElevation = SearchBarDefaults.ShadowElevation,
        windowInsets = SearchBarDefaults.windowInsets,
        content = {},
    )
}

@Composable
fun SavedNewsScreen(modifier: Modifier = Modifier) {
    Text(text = "Saved News", modifier = modifier.padding(16.dp))
}