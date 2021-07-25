package com.godaddy.namesearch.repository.storage

data class NewsSource(
    val id: String,
    val name: String
)

data class NewsArticleItem(
    val source: NewsSource,
    val author: String,
    val title: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    val content: String
)

data class NewsFeedItem(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticleItem>
)