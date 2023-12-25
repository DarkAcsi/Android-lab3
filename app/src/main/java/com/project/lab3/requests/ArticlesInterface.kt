package com.project.lab3.requests

import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesInterface {
    @GET("/api/1/news")
    suspend fun getArticles(
        @Query("apikey") apikey: String,
        @Query("q") request: String,
    ): RequestArticles

    @GET("/api/1/news")
    suspend fun getArticlesNewPage(
        @Query("apikey") apikey: String,
        @Query("q") request: String,
        @Query("page") newPage: String,
    ): RequestArticles

}
