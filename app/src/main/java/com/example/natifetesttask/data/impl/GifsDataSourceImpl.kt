package com.example.natifetesttask.data

import com.example.natifetesttask.model.GifApiResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class GifsDataSourceImpl @Inject constructor(private val service: GifRestApiService): GifsDataSource {

    override suspend fun getNextPage(pageNum: Int, pageSize: Int, query: String?): GifApiResponse? {
        return if (query != null && query.isNotEmpty()) {
            service.getGifs(API_KEY, query, pageSize, pageNum * pageSize).body()
        } else {
            service.getGifs(API_KEY, pageSize, pageNum * pageSize).body()
        }
    }

    companion object {
        const val API_KEY = "rVPfyGnRm8dLauVvoXCNxmzZdkW0yQ84"
        const val BASE_URL = "https://api.giphy.com/v1/"
    }

}