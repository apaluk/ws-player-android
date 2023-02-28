package com.apaluk.wsplayer.domain.repository

import com.apaluk.wsplayer.domain.model.search.SearchHistoryItem
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getFilteredHistory(filter: String): Flow<List<SearchHistoryItem>>
    suspend fun addToHistory(input: String)
    suspend fun deleteFromHistory(input: String)
}