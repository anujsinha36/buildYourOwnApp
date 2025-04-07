package com.example.sokasinging.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokasinging.data.model.Song
import com.example.sokasinging.domain.usecases.GetSongsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SongsListUiState(
    val songs: List<Song> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SongsListViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SongsListUiState())
    val uiState: StateFlow<SongsListUiState> = _uiState.asStateFlow()

    init {
        getSongs()
    }

    fun getSongs() {
        viewModelScope.launch {
            _uiState.value = SongsListUiState(isLoading = true)

            try {
                getSongsUseCase().collect { result ->
                    _uiState.value = SongsListUiState(songs = result)
                }
            } catch (e: Exception) {
                _uiState.value = SongsListUiState(error = e.message)

            }
        }

    }
}