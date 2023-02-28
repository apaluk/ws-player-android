package com.apaluk.wsplayer.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apaluk.wsplayer.data.database.model.SearchHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    @Query("SELECT * FROM searchHistory WHERE input=:entry")
    suspend fun getEntry(entry: String): SearchHistory?

    @Query("SELECT * FROM searchHistory WHERE input LIKE '%' || :filter || '%' ORDER BY date DESC")
    fun filterSearchHistory(filter: String): Flow<List<SearchHistory>>

    @Query("DELETE FROM searchHistory WHERE input=:entry")
    suspend fun delete(entry: String)
}