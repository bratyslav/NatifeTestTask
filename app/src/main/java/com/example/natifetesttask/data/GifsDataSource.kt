package com.example.natifetesttask.data

import com.example.natifetesttask.model.GifApiResponse

interface GifsDataSource {

    suspend fun getNextPage(pageNum: Int, pageSize: Int = 16, query: String? = null): GifApiResponse?

}