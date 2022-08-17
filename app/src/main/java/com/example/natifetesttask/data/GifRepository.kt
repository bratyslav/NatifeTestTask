package com.example.natifetesttask.data

import com.example.natifetesttask.model.Gif
import javax.inject.Inject

class GifRepository @Inject constructor(private val dataSource: GifsDataSource) {

    suspend fun loadNextPage(pageNum: Int, pageSize: Int = 16, query: String? = null): List<Gif>? {
        return if (InternetService.isNetworkAvailable()) {
            dataSource.getNextPage(pageNum, pageSize, query)?.data
        } else {
            null
        }
    }

}