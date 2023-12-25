package com.project.lab3.requests

data class RequestArticles(
    val Status: String,
    val totalResults: Int,
    val results: List<Article>,
    val nextPage: String?,

    )
