package com.example.natifetesttask

import android.content.Context
import com.example.natifetesttask.data.GifRepository
import com.example.natifetesttask.data.GifDataSourceModule
import com.example.natifetesttask.data.RetrofitModule
import dagger.Component


@Component(modules = [GifDataSourceModule::class, RetrofitModule::class])
interface ApplicationGraph {

    val gifRepository: GifRepository

}

class AppContainer(val context: Context, daggerAppGraph: ApplicationGraph) {

    val gifRepository = daggerAppGraph.gifRepository

}