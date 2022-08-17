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

data class MainScreenUiState(
    val gifs: List<Gif> = emptyList(),
    val searchValue: String = ""
)

class MainScreenViewModel(private val gifRepository: GifRepository): ViewModel() {

    private val _uiState = MutableStateFlow(MainScreenUiState())
    private var currentPageNum = 1
    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val gifs = gifRepository.getPage(0)
            gifs?.let { _uiState.update { it.copy(gifs = gifs) } }
        }
    }

    fun onSearchValueChange(newValue: String) {
        _uiState.update { it.copy(searchValue = newValue) }
    }

    fun applySearchValue() {
        _uiState.update { it.copy(gifs = emptyList()) }
        val searchQuery = uiState.value.searchValue.ifEmpty { null }
        viewModelScope.launch {
            val gifs = gifRepository.getPage(currentPageNum, query = searchQuery)
            gifs?.let { _uiState.update { it.copy(gifs = gifs) } }
            currentPageNum = 1
        }
    }

    fun loadNextGifs() {
        viewModelScope.launch {
            val searchQuery = uiState.value.searchValue.ifEmpty { null }
            val nextGifs = gifRepository.getPage(currentPageNum, query = searchQuery)
            nextGifs?.let { _uiState.update { it.copy(gifs = it.gifs + nextGifs) } }
            currentPageNum += 1
        }
    }

    companion object {
        // used to save ui state between configuration changes
        fun provideFactory(gifRepository: GifRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainScreenViewModel(gifRepository) as T
                }
            }
    }

}