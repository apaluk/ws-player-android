package com.apaluk.wsplayer.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.core.util.Resource
import com.apaluk.wsplayer.data.stream_cinema.StreamCinemaRepository
import com.apaluk.wsplayer.data.webshare.WebShareRepository
import com.apaluk.wsplayer.domain.model.media.Media
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            streamCinemaRepository.mediaFilter().collect { resource ->
                _uiState.update {
                    when (resource) {
                        is Resource.Error -> it.copy(
                            isLoading = false,
                            errorToast = resource.message ?: "Something went wrong"
                        )
                        is Resource.Loading -> it.copy(isLoading = true)
                        is Resource.Success -> it.copy(
                            isLoading = false,
                            errorToast = null,
                            items = resource.data.orEmpty()
                        )
                    }
                }
            }
        }
    }

    fun onMediaSelected(mediaId: String) {
        _uiState.update { it.copy(selectedMediaId = mediaId) }
    }

    fun clearSelectedMedia() {
        _uiState.update { it.copy(selectedMediaId = null) }
    }

    fun onSearchTextChanged(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun triggerSearch() {
        Timber.d("xxx search!")
        viewModelScope.launch {
            val res = streamCinemaRepository.search(_uiState.value.searchText).last()
            Timber.d("xxx search result $res")
        }
    }
}

data class DashboardUiState(
    val isLoading: Boolean = true,
    val items: List<Media> = emptyList(),
    val selectedMediaId: String? = null,
    val searchText: String = "",
    val errorToast: String? = null
)