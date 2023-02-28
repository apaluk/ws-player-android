package com.apaluk.wsplayer.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchHistory")
data class WatchHistory(
    @PrimaryKey val id: Long,
    val mediaId: String,
    val seasonId: String?,
    val episodeId: String?,
    val streamId: String,
    val progressSeconds: Int,
    val lastUpdate: Long,
    val isWatched: Boolean
)