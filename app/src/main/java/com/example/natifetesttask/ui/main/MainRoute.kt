package com.example.natifetesttask.ui.main

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.natifetesttask.R

@Composable
fun MainScreen(navController: NavController, viewModel: MainScreenViewModel, context: Context) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchLine(state, viewModel)
        GifsGrid(state, viewModel, context)
    }

}

@Composable
fun SearchLine(state: MainScreenUiState, viewModel: MainScreenViewModel) {
    Row(
        Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        TextField(
            value = state.searchValue,
            onValueChange = { newValue -> viewModel.onSearchValueChange(newValue) }
        )
        Button(onClick = { viewModel.applySearchValue() }) {
            Text("Search")
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifsGrid(state: MainScreenUiState, viewModel: MainScreenViewModel, context: Context) {
    val scrollState = rememberLazyListState()
    val endOfListReached by remember {
        derivedStateOf { scrollState.isScrolledToTheEnd() }
    }
    LaunchedEffect(endOfListReached) {
        viewModel.loadNextGifs()
    }

    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    LazyVerticalGrid(
        cells = GridCells.Adaptive(120.dp),
        state = scrollState
    ) {
        items(state.gifs.size) { index ->
            val url = state.gifs[index].images?.previewGif?.url!!
            Gif(url, context, imageLoader)
        }
    }
}

@Composable
fun Gif(url: String, context: Context, imageLoader: ImageLoader) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(url)
                .build(),
            imageLoader = imageLoader,
            placeholder = painterResource(id = R.drawable.ic_launcher_background)
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.height(150.dp)
    )
}


fun LazyListState.isScrolledToTheEnd() : Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}