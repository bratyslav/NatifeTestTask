package com.example.natifetesttask.data

import com.example.natifetesttask.data.GifRestApiService.Companion.BASE_URL
import com.example.natifetesttask.data.impl.GifsDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
abstract class GifDataSourceModule {

    @Binds
    abstract fun gifsDataSourceImpl(gifsDataSource: GifsDataSourceImpl): GifsDataSource

}

@Module
class RetrofitModule {

    @Provides
    fun provideGifRestApiService(): GifRestApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(GifRestApiService::class.java)
    }

}