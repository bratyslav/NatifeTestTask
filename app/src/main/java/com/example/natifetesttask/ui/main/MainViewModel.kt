package com.example.natifetesttask.ui.main

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

data class MainUiState(
    val gifs: List<Gif> = emptyList(),
    val currentPageNum: Int = 0,
    val searchQuery: String = ""
)

class MainViewModel(private val gifRepository: GifRepository): ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val gifs = gifRepository.loadNextPage(0)
            gifs?.let { _uiState.update { it.copy(gifs = gifs) } }
        }
    }

    fun onSearchQueryChange(newValue: String) {
        if (isValid(newValue)) {
            _uiState.update { it.copy(searchQuery = newValue) }
        }
    }

    fun applySearchQuery() {
        _uiState.update { it.copy(gifs = emptyList()) }
        val searchQuery = uiState.value.searchQuery.ifEmpty { null }
        viewModelScope.launch {
            val gifs = gifRepository.loadNextPage(uiState.value.currentPageNum, query = searchQuery)
            gifs?.let {
                _uiState.update {
                    it.copy(gifs = gifs, currentPageNum = it.currentPageNum + 1)
                }
            }
        }
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

    private fun isValid(value: String): Boolean {
        return !value.contains("\n")
    }

    companion object {
        // used to save ui state between configuration changes
        fun provideFactory(gifRepository: GifRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(gifRepository) as T
                }
            }
    }

}