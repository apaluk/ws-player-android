package com.apaluk.wsplayer.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "streams")
data class Streams(
    @PrimaryKey val id: Long,
    val definition: Int,
    val speed: Float,
    val language: String?
)