plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.apaluk.wsplayer"
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        applicationId = "com.apaluk.wsplayer"
        minSdk = Config.minSdkVersion
        targetSdk = Config.compileSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.lifecycleRuntime)
    implementation(Dependencies.AndroidX.activityCompose)

    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiToolingPreview)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.navigation)

    implementation(Dependencies.retrofit)
    implementation(Dependencies.guava)
    implementation(Dependencies.simpleXml)
    implementation(Dependencies.timber)

    implementation(Dependencies.Hilt.hilt)
    implementation(Dependencies.Hilt.navigationCompose)
    kapt(Dependencies.Hilt.hiltCompiler)
    kapt(Dependencies.Hilt.daggerHiltCompiler)

    testImplementation(Dependencies.Test.jUnit)
    androidTestImplementation(Dependencies.AndroidTest.jUnitExt)
    androidTestImplementation(Dependencies.AndroidTest.espresso)
    androidTestImplementation(Dependencies.AndroidTest.composeUiTest)
    debugImplementation(Dependencies.Debug.composeUi)
    debugImplementation(Dependencies.Debug.composeUiTooling)
}