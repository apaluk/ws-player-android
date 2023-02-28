package com.apaluk.wsplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apaluk.wsplayer.data.database.dao.SearchHistoryDao
import com.apaluk.wsplayer.data.database.model.Favorites
import com.apaluk.wsplayer.data.database.model.SearchHistory
import com.apaluk.wsplayer.data.database.model.Streams
import com.apaluk.wsplayer.data.database.model.WatchHistory

@Database(
    entities = [ WatchHistory::class, SearchHistory::class, Streams::class, Favorites::class ],
    version = 1,
    exportSchema = false
)
abstract class WspDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}