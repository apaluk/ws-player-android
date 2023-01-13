object Dependencies {

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"
        const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.lifecycleRuntime}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val material = "androidx.compose.material3:material3:${Versions.material}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.compose}"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
    const val simpleXml = "org.simpleframework:simple-xml:${Versions.simpleXml}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val okHttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLoggingInterceptor}"

    object Test {
        const val jUnit = "junit:junit:${Versions.jUnit}"
    }

    object AndroidTest {
        const val jUnitExt = "androidx.test.ext:junit:${Versions.jUnitExt}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val composeUiTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    }

    object Debug {
        const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
        const val composeUi = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"
    }

    object Hilt {
        const val gradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}"
        const val hilt = "com.google.dagger:hilt-android:${Versions.hilt}"
        const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltAndroidX}"
        const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
        const val navigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltAndroidX}"
    }

    object Moshi {
        const val converter = "com.squareup.retrofit2:converter-moshi:${Versions.moshiConverter}"
        const val moshi = "com.squareup.moshi:moshi:${Versions.moshi}"
        const val adapters = "com.squareup.moshi:moshi-adapters:${Versions.moshi}"
        const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"
    }

    object ExoPlayer {
        const val exoPlayer = "androidx.media3:media3-exoplayer:${Versions.exoPlayer}"
        const val ui = "androidx.media3:media3-ui:${Versions.exoPlayer}"
        const val okHttpDataSource = "androidx.media3:media3-datasource-okhttp:${Versions.exoPlayer}"
    }

    const val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore}"
    const val coil = "io.coil-kt:coil-compose:${Versions.coil}"
}