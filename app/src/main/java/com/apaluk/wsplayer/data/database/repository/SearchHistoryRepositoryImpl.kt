package com.apaluk.wsplayer.data.database.repository

import com.apaluk.wsplayer.data.database.dao.SearchHistoryDao
import com.apaluk.wsplayer.data.database.mapper.toSearchHistoryItem
import com.apaluk.wsplayer.data.database.model.SearchHistory
import com.apaluk.wsplayer.domain.model.search.SearchHistoryItem
import com.apaluk.wsplayer.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
): SearchHistoryRepository {
    override fun getFilteredHistory(filter: String): Flow<List<SearchHistoryItem>> =
        searchHistoryDao.filterSearchHistory(filter).map { list ->
            list.map { it.toSearchHistoryItem() }
        }

    override suspend fun addToHistory(input: String) {
        val lowerCaseInput = input.lowercase()
        val entry = searchHistoryDao.getEntry(lowerCaseInput)?.let {
            it.copy(
                count = it.count + 1,
                date = System.currentTimeMillis()
            )
        } ?: SearchHistory(
            id = 0L,
            input = lowerCaseInput,
            date = System.currentTimeMillis()
        )
        searchHistoryDao.upsert(entry)
    }

    override suspend fun deleteFromHistory(input: String) =
        searchHistoryDao.delete(input.lowercase())
}