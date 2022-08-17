package com.example.natifetesttask.ui.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.natifetesttask.data.GifRepository
import com.example.natifetesttask.model.Gif
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PreviewUiState(
    val currentGifNum: Int,
    val currentPageNum: Int,
    val gifs: List<Gif>,
    val searchQuery: String
)

class PreviewVewModel(
    private val gifRepository: GifRepository,
    currentGifNum: Int,
    searchQuery: String
): ViewModel() {
    private val _uiState: MutableStateFlow<PreviewUiState>
    val uiState: StateFlow<PreviewUiState>

    init {
        val currentPageNum = loadedGifs.size / 16
        _uiState = MutableStateFlow(PreviewUiState(currentGifNum, currentPageNum, loadedGifs, searchQuery))
        uiState = _uiState.asStateFlow()
    }

    fun loadNextGifs() {
        viewModelScope.launch {
            val searchQuery = uiState.value.searchQuery.ifEmpty { null }
            val nextGifs = gifRepository.loadNextPage(uiState.value.currentPageNum, query = searchQuery)
            nextGifs?.let {
                _uiState.update {
                    it.copy(gifs = it.gifs + nextGifs, currentPageNum = it.currentPageNum + 1)
                }
            }
        }
    }

    companion object {
        // used to save ui state between configuration changes
        fun provideFactory(
            gifRepository: GifRepository,
            currentGifNum: Int,
            searchQuery: String
        ): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PreviewVewModel(gifRepository, currentGifNum, searchQuery) as T
                }
            }

        var loadedGifs: List<Gif> = emptyList()
    }

}