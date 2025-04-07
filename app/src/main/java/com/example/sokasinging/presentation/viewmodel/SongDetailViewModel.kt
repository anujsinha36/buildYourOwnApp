package com.example.sokasinging.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sokasinging.data.model.SongDetails
import com.example.sokasinging.domain.usecases.GetSongDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class SongDetailUiState(
    val songDetails: SongDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SongDetailViewModel @Inject constructor(
    private val getSongDetailsUseCase: GetSongDetailsUseCase):
    ViewModel() {
    private val _uiState = MutableStateFlow(SongDetailUiState())
    val uiState: StateFlow<SongDetailUiState> = _uiState.asStateFlow()

    fun loadSongDetails(songName: String) {
        viewModelScope.launch {
            _uiState.value = SongDetailUiState(isLoading = true)

            try {
                getSongDetailsUseCase(songName). collect { result ->
                    _uiState.value = SongDetailUiState(songDetails = result)
                }
            }
            catch(e: Exception){
                _uiState.value = SongDetailUiState(error = e.message)
            }
            //isLoading value for both try and catch block will be false

        }
    }
}