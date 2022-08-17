package com.example.natifetesttask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.natifetesttask.data.InternetService
import com.example.natifetesttask.ui.App
import com.example.natifetesttask.ui.theme.NatifeTestTaskTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideSystemUI()

        InternetService.initialize(applicationContext)
        val applicationGraph: ApplicationGraph = DaggerApplicationGraph.create()
        val appContainer = AppContainer(applicationContext, applicationGraph)

        setContent {
            NatifeTestTaskTheme {
                App(appContainer)
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

}