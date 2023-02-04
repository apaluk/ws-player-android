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

    fun onSearchScreenAction(action: SearchScreenAction) {
        when(action) {
            is SearchScreenAction.SearchTextChanged -> onSearchTextChanged(action.text)
            is SearchScreenAction.MediaSelected ->
                if(action.mediaId == null) clearSelectedMedia() else onMediaSelected(action.mediaId)
            SearchScreenAction.TriggerSearch -> triggerSearch()
            SearchScreenAction.ClearSearch -> clearSearch()
        }
    }

    private fun onMediaSelected(mediaId: String) {
        _uiState.update { it.copy(selectedMediaId = mediaId) }
    }

    private fun clearSelectedMedia() {
        _uiState.update { it.copy(selectedMediaId = null) }
    }

    private fun onSearchTextChanged(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    private fun triggerSearch() {
        viewModelScope.launch {
            _uiState.update { it.copy(searchState = UiState.Loading, scrollListToTop = true) }
            val searchResource = streamCinemaRepository.search(_uiState.value.searchText.trim()).last()
            _uiState.update { it.copy(
                searchResults = searchResource.data.orEmpty(),
                searchState = searchResource.toUiState(),
                scrollListToTop = false
            ) }
        }
    }

    private fun clearSearch() {
        _uiState.update { it.copy(searchText = "", searchResults = emptyList()) }
    }
}

data class SearchUiState(
    val selectedMediaId: String? = null,
    val searchText: String = "",
    val errorToast: String? = null,
    val searchResults: List<SearchResultItem> = emptyList(),
    val searchState: UiState = UiState.Idle,
    val scrollListToTop: Boolean = false
)

sealed class SearchScreenAction {
    data class SearchTextChanged(val text: String): SearchScreenAction()
    object TriggerSearch: SearchScreenAction()
    object ClearSearch: SearchScreenAction()
    data class MediaSelected(val mediaId: String?): SearchScreenAction()
}
