package com.apaluk.wsplayer.data.database.mapper

import com.apaluk.wsplayer.data.database.model.SearchHistory
import com.apaluk.wsplayer.domain.model.search.SearchHistoryItem
import java.util.*

fun SearchHistory.toSearchHistoryItem(): SearchHistoryItem = SearchHistoryItem(
    text = input,
    lastSearchDate = Date(date)
)