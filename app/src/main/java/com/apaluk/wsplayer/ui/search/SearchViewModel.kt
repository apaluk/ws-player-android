package com.apaluk.wsplayer.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apaluk.wsplayer.data.stream_cinema.StreamCinemaRepository
import com.apaluk.wsplayer.domain.model.media.SearchResultItem
import com.apaluk.wsplayer.ui.common.util.UiState
import com.apaluk.wsplayer.ui.common.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val streamCinemaRepository: StreamCinemaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

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
        viewModelScope.launch {
            _uiState.update { it.copy(searchState = UiState.Loading, scrollListToTop = true) }
            val searchResource = streamCinemaRepository.search(_uiState.value.searchText).last()
            _uiState.update { it.copy(
                searchResults = searchResource.data.orEmpty(),
                searchState = searchResource.toUiState(),
                scrollListToTop = false
            ) }
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchText = "", searchResults = emptyList()) }
    }
}

data class SearchUiState(
    val selectedMediaId: String? = null,    // TODO nechat?
    val searchText: String = "",
    val errorToast: String? = null,
    val searchResults: List<SearchResultItem> = emptyList(),
    val searchState: UiState = UiState.Idle,
    val scrollListToTop: Boolean = false
)
