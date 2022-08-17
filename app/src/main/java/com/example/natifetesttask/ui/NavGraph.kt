package com.example.natifetesttask.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.natifetesttask.AppContainer
import com.example.natifetesttask.ui.main.MainRoute
import com.example.natifetesttask.ui.main.MainViewModel
import com.example.natifetesttask.ui.preview.PreviewRoute
import com.example.natifetesttask.ui.preview.PreviewVewModel

@Composable
fun NavGraph(
    appContainer: AppContainer,
    navController: NavHostController,
    startDestination: String = Destinations.MAIN_ROUTE
) {
    NavHost(navController, startDestination) {
        composable(Destinations.MAIN_ROUTE) {
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModel.provideFactory(appContainer.gifRepository)
            )
            MainRoute(navController, viewModel, appContainer.context)
        }

        composable(
            "${Destinations.PREVIEW_ROUTE}?gifNum={gifNum}&searchQuery={searchQuery}",
            arguments = listOf(
                navArgument("gifNum") { defaultValue = 0 },
                navArgument("searchQuery") { defaultValue = "" }
            )
        ) {
            val gifNum = it.arguments?.getInt("gifNum")!!
            val searchQuery = it.arguments?.getString("searchQuery")!!

            val viewModel: PreviewVewModel = viewModel(
                factory = PreviewVewModel.provideFactory(
                    appContainer.gifRepository,
                    gifNum,
                    searchQuery
                )
            )
            PreviewRoute(viewModel, appContainer.context)
        }
    }
}

object Destinations {
    const val MAIN_ROUTE = "mainScreen"
    const val PREVIEW_ROUTE = "gifPreview"
}