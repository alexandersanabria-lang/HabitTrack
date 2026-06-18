package com.habittrack.data.remote

import retrofit2.http.GET

interface ZenQuotesApiService {

    @GET("random")
    suspend fun getRandomQuote(): List<QuoteDto>
}