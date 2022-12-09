package com.apaluk.wsplayer.data.di

import com.apaluk.wsplayer.data.webshare.remote.WebShareApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideWebshareApi(): WebShareApi {
        return Retrofit.Builder()
            .baseUrl("https://webshare.cz")
            .build()
            .create(WebShareApi::class.java)
    }

}