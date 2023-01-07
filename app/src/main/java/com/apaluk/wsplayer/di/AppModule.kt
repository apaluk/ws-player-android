package com.apaluk.wsplayer.di

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @ApplicationScope
    @Provides
    fun provideApplicationScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycleScope
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope