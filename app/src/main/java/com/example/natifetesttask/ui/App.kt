package com.example.natifetesttask.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.natifetesttask.AppContainer

@Composable
fun App(appContainer: AppContainer) {

    val navController = rememberNavController()

    NavGraph(appContainer, navController)

}

