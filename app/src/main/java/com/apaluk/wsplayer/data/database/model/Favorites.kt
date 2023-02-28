package com.apaluk.wsplayer.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class Favorites(
    @PrimaryKey val id: Long,
    val mediaId: String
)
