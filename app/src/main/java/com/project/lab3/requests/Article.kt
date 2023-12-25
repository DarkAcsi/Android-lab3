package com.project.lab3.requests

import android.net.Uri

data class Article(
    val article_id: String,
    val title: String,
    val link: String?,
    val source_id: Any?,
    val source_priority: Any?,
    val keywords: Any?,
    val creator: Any?,
    val image_url: String?,
    val video_url: Any?,
    val description: String?,
    val pubDate: String?,
    val content: String?,
    val country: Any?,
    val category: Any?,
    val language: Any?,
)