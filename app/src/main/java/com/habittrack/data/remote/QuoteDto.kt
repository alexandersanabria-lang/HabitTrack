package com.habittrack.data.remote

import com.google.gson.annotations.SerializedName

data class QuoteDto(
    @SerializedName("q")
    val quote: String,
    @SerializedName("a")
    val author: String
)