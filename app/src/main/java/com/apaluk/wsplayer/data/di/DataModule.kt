package com.apaluk.wsplayer.data.di

import com.apaluk.wsplayer.BuildConfig
import com.apaluk.wsplayer.core.util.Constants
import com.apaluk.wsplayer.data.stream_cinema.StreamCinemaRepositoryImpl
import com.apaluk.wsplayer.data.stream_cinema.remote.StreamCinemaApi
import com.apaluk.wsplayer.data.webshare.remote.WebShareApi
import com.apaluk.wsplayer.domain.repository.StreamCinemaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

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
    fun provideStreamCinemaApi(
        okHttpClient: OkHttpClient
    ): StreamCinemaApi {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://plugin.sc2.zone")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StreamCinemaApi::class.java)
    }

    @Provides
    fun provideStreamCinemaRepository(
        streamCinemaApi: StreamCinemaApi
    ): StreamCinemaRepository {
        return StreamCinemaRepositoryImpl(streamCinemaApi)
    }
}