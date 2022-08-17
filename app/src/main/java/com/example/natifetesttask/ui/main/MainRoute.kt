package com.example.natifetesttask.ui.main

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.natifetesttask.R
import com.example.natifetesttask.ui.Destinations
import com.example.natifetesttask.ui.GifLoader
import com.example.natifetesttask.ui.preview.PreviewVewModel
import com.example.natifetesttask.ui.theme.Purple200

@Composable
fun MainRoute(navController: NavController, viewModel: MainViewModel, context: Context) {

    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchLine(state, viewModel)
        GifsGrid(state, viewModel, navController, context)
    }

}

@Composable
fun SearchLine(state: MainUiState, viewModel: MainViewModel) {
    Row(
        Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = state.searchQuery,
            onValueChange = { newValue -> viewModel.onSearchQueryChange(newValue) },
            modifier = Modifier
                .border(1.dp, Purple200, shape = RoundedCornerShape(4.dp))
                .height(50.dp)
        )
        Button(onClick = { viewModel.applySearchQuery() }) {
            Text("Search", color = Color.White)
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifsGrid(
    state: MainUiState,
    viewModel: MainViewModel,
    navController: NavController,
    context: Context
) {
    val scrollState = rememberLazyListState()
    val endOfListReached by remember {
        derivedStateOf { scrollState.isScrolledToTheEnd() }
    }
    LaunchedEffect(endOfListReached) {
        viewModel.loadNextGifs()
    }

    LazyVerticalGrid(
        cells = GridCells.Adaptive(120.dp),
        state = scrollState
    ) {
        items(state.gifs.size) { index ->
            val url = state.gifs[index].images?.previewGif?.url!!
            Gif(url, context, navController, state, index)
        }
    }
}

@Composable
fun Gif(
    url: String,
    context: Context,
    navController: NavController,
    state: MainUiState,
    index: Int
) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context)
                .data(url)
                .build(),
            imageLoader = GifLoader.getInstance(context),
            placeholder = painterResource(id = R.drawable.empty)
        ),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.height(150.dp).clickable {
            PreviewVewModel.loadedGifs = state.gifs
            val route = "${Destinations.PREVIEW_ROUTE}?gifNum=${index}&searchQuery=${state.searchQuery}"
            navController.navigate(route)
        }
    )
}


fun LazyListState.isScrolledToTheEnd() : Boolean {
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
    return lastItem == null || lastItem.size + lastItem.offset <= layoutInfo.viewportEndOffset
}