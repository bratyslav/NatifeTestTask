package com.example.natifetesttask.data.impl

import com.example.natifetesttask.data.GifRestApiService
import com.example.natifetesttask.data.GifRestApiService.Companion.API_KEY
import com.example.natifetesttask.data.GifsDataSource
import com.example.natifetesttask.model.GifApiResponse
import javax.inject.Inject

class GifsDataSourceImpl @Inject constructor(private val service: GifRestApiService): GifsDataSource {

    override suspend fun getNextPage(pageNum: Int, pageSize: Int, query: String?): GifApiResponse? {
        return if (query != null && query.isNotEmpty()) {
            service.getGifs(API_KEY, query, pageSize, pageNum * pageSize).body()
        } else {
            service.getGifs(API_KEY, pageSize, pageNum * pageSize).body()
        }
    }

}