package com.apaluk.wsplayer.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.apaluk.wsplayer.data.database.model.MediaInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaInfoDao {

    @Upsert
    suspend fun upsertMediaInfo(mediaInfo: MediaInfo)

    @Query("SELECT * FROM mediaInfo WHERE mediaId=:mediaId")
    fun getMediaInfo(mediaId: String): Flow<MediaInfo?>

}