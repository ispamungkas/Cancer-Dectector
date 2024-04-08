package com.dicoding.asclepius.datasouce.network

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.model.ResponseHealty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top-headlines")
    suspend fun getArticle(
        @Query("q") searchName: String? = "cancer",
        @Query("category") categorySearch: String? = "health",
        @Query("language") language: String? = "en",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ) : Response<ResponseHealty>

}