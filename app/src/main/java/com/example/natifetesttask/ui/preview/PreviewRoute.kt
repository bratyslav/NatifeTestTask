package com.example.natifetesttask.ui.preview

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.natifetesttask.R
import com.example.natifetesttask.ui.GifLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PreviewRoute(viewModel: PreviewVewModel, context: Context) {

    val state by viewModel.uiState.collectAsState()

    val pagerState = rememberPagerState(initialPage = state.currentGifNum)
    val configuration = LocalConfiguration.current
    val imageModifier = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.fillMaxHeight()
    }

    HorizontalPager(count = state.gifs.size, state = pagerState) { pageIndex ->
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context)
                    .data(state.gifs[pageIndex].images?.original?.url)
                    .build(),
                imageLoader = GifLoader.getInstance(context),
                placeholder = painterResource(id = R.drawable.empty)
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
        if (state.gifs.size - 3 < pageIndex) {
            viewModel.loadNextGifs()
        }
    }

}