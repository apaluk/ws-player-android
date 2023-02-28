package com.apaluk.wsplayer.core.testing

import com.apaluk.wsplayer.domain.model.search.SearchHistoryItem
import com.apaluk.wsplayer.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.util.*

class SearchHistoryRepositoryFake: SearchHistoryRepository {
    private val history = mutableListOf<SearchHistoryItem>()

    override fun getFilteredHistory(filter: String): Flow<List<SearchHistoryItem>> =
        flowOf(
            history.filter { it.text.contains(filter.lowercase()) }
        )

    override suspend fun addToHistory(input: String) {
        history.add(SearchHistoryItem(input, lastSearchDate = Date()))
    }

    override suspend fun deleteFromHistory(input: String) {
        history.removeIf { it.text == input }
    }
}