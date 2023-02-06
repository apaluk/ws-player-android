package com.apaluk.wsplayer.di

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.apaluk.wsplayer.core.login.LoginManager
import com.apaluk.wsplayer.core.login.LoginManagerImpl
import com.apaluk.wsplayer.core.settings.AppSettings
import com.apaluk.wsplayer.data.webshare.WebShareRepository
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

    @Provides
    fun provideLoginManager(
        @ApplicationScope scope: CoroutineScope,
        appSettings: AppSettings,
        webShareRepository: WebShareRepository
    ): LoginManager {
        return LoginManagerImpl(scope, appSettings, webShareRepository)
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope