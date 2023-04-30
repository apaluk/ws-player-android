package com.apaluk.wsplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.apaluk.wsplayer.data.database.dao.MediaInfoDao
import com.apaluk.wsplayer.data.database.dao.SearchHistoryDao
import com.apaluk.wsplayer.data.database.dao.WatchHistoryDao
import com.apaluk.wsplayer.data.database.model.*

@Database(
    entities = [
        WatchHistory::class,
        SearchHistory::class,
        Stream::class,
        Favorites::class,
        MediaInfo::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WspDatabase: RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun mediaInfoDao(): MediaInfoDao
}