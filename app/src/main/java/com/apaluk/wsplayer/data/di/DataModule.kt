package com.apaluk.wsplayer.data.di

import android.content.Context
import androidx.room.Room
import com.apaluk.wsplayer.BuildConfig
import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.data.database.WspDatabase
import com.apaluk.wsplayer.data.database.dao.MediaInfoDao
import com.apaluk.wsplayer.data.database.dao.SearchHistoryDao
import com.apaluk.wsplayer.data.database.dao.WatchHistoryDao
import com.apaluk.wsplayer.data.database.repository.MediaInfoRepositoryImpl
import com.apaluk.wsplayer.data.database.repository.SearchHistoryRepositoryImpl
import com.apaluk.wsplayer.data.database.repository.WatchHistoryRepositoryImpl
import com.apaluk.wsplayer.data.stream_cinema.StreamCinemaRepositoryImpl
import com.apaluk.wsplayer.data.stream_cinema.remote.StreamCinemaApi
import com.apaluk.wsplayer.data.stream_cinema.remote.adapter.MediaTypeAdapter
import com.apaluk.wsplayer.data.stream_cinema.remote.adapter.TvShowChildTypeAdapter
import com.apaluk.wsplayer.data.webshare.remote.WebShareApi
import com.apaluk.wsplayer.domain.repository.MediaInfoRepository
import com.apaluk.wsplayer.domain.repository.SearchHistoryRepository
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import com.apaluk.wsplayer.domain.repository.WatchHistoryRepository
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.HTTP_TIMEOUT_CONNECT_SEC, TimeUnit.SECONDS)
            .writeTimeout(Constants.HTTP_TIMEOUT_WRITE_SEC, TimeUnit.SECONDS)
            .readTimeout(Constants.HTTP_TIMEOUT_READ_SEC, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    val loggingInterceptor = HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                    addInterceptor(loggingInterceptor)
                }
            }
            .build()
    }

    @Provides
    fun provideWebshareApi(
        okHttpClient: OkHttpClient
    ): WebShareApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://webshare.cz")
            .build()
            .create(WebShareApi::class.java)
    }

    @Provides
    @StreamCinemaMoshi
    fun provideStreamCinemaMoshi(): Moshi {
        return Moshi.Builder()
            .add(MediaTypeAdapter())
            .add(TvShowChildTypeAdapter())
            .build()
    }
    

    @Provides
    fun provideStreamCinemaApi(
        okHttpClient: OkHttpClient,
        @StreamCinemaMoshi streamCinemaMoshi: Moshi
    ): StreamCinemaApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://plugin.sc2.zone")
            .addConverterFactory(MoshiConverterFactory.create(streamCinemaMoshi))
            .build()
            .create(StreamCinemaApi::class.java)
    }

    @Provides
    fun provideWspDatabase(
        @ApplicationContext context: Context
    ): WspDatabase =
        Room.databaseBuilder(context, WspDatabase::class.java, "wsPlayerDatabase").build()

    @Provides
    fun provideStreamCinemaRepository(
        streamCinemaApi: StreamCinemaApi
    ): StreamCinemaRepository = StreamCinemaRepositoryImpl(streamCinemaApi)

    @Provides
    fun provideSearchHistoryRepository(
        searchHistoryDao: SearchHistoryDao
    ): SearchHistoryRepository = SearchHistoryRepositoryImpl(searchHistoryDao)

    @Singleton
    @Provides
    fun provideWatchHistoryRepository(
        watchHistoryDao: WatchHistoryDao
    ): WatchHistoryRepository = WatchHistoryRepositoryImpl(watchHistoryDao)

    @Provides
    fun provideMediaInfoRepository(
        mediaInfoDao: MediaInfoDao
    ): MediaInfoRepository = MediaInfoRepositoryImpl(mediaInfoDao)

    @Provides
    fun provideSearchHistoryDao(
        wspDatabase: WspDatabase
    ): SearchHistoryDao = wspDatabase.searchHistoryDao()

    @Provides
    fun provideWatchHistoryDao(
        wspDatabase: WspDatabase
    ): WatchHistoryDao = wspDatabase.watchHistoryDao()

    @Provides
    fun provideMediaInfoDao(
        wspDatabase: WspDatabase
    ): MediaInfoDao = wspDatabase.mediaInfoDao()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StreamCinemaMoshi