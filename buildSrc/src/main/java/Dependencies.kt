object Dependencies {

    object AndroidX {
        const val core = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycleRuntime}"
        const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    }

    object Compose {
        const val ui = "androidx.compose.ui:ui:${Versions.compose}"
        const val uiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"
        const val material = "androidx.compose.material3:material3:${Versions.material}"
    }

    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
    const val simpleXml = "org.simpleframework:simple-xml:${Versions.simpleXml}"

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
        const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltCompiler}"
        const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:${Versions.hilt}"
    }
}