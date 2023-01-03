package com.apaluk.wsplayer.data.stream_cinema.remote.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaDetailsResponseDto(
    @Json(name = "available_streams")
    val availableStreams: AvailableStreamsDto,
    @Json(name = "cast")
    val cast: List<CastDto>,
    @Json(name = "children_count")
    val childrenCount: Int,
    @Json(name = "i18n_info_labels")
    val i18nInfoLabels: List<I18nInfoLabelDto>,
    @Json(name = "_id")
    val id: String,
    @Json(name = "info_labels")
    val infoLabels: InfoLabelsDto,
    @Json(name = "is_concert")
    val isConcert: Boolean,
    @Json(name = "parent_info_labels")
    val parentInfoLabels: ParentInfoLabelsDto,
    @Json(name = "play_count")
    val playCount: Int,
    @Json(name = "ratings")
    val ratings: RatingsDto,
    @Json(name = "root_parent")
    val rootParent: String,
    @Json(name = "services")
    val services: ServicesDto,
    @Json(name = "stream_info")
    val streamInfo: StreamInfoDto,
    @Json(name = "videos")
    val videos: List<Any>
)