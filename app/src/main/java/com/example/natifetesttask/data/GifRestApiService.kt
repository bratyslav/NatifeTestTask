package com.example.natifetesttask.data

import com.example.natifetesttask.model.GifApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GifRestApiService {

    @GET("gifs/trending")
    suspend fun getGifs(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<GifApiResponse>

    @GET("gifs/search")
    suspend fun getGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String, // search string
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): Response<GifApiResponse>

    companion object {
        const val API_KEY = "rVPfyGnRm8dLauVvoXCNxmzZdkW0yQ84"
        const val BASE_URL = "https://api.giphy.com/v1/"
    }

}