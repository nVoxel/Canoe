plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.voxeldev.canoe.utils"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        resValue("string", "wakatime_api_base_url", extra["WAKATIME_API_BASE_URL"].toString())
        resValue("string", "wakatime_oauth_base_url", extra["WAKATIME_OAUTH_BASE_URL"].toString())
        resValue("string", "wakatime_photo_base_url", extra["WAKATIME_PHOTO_BASE_URL"].toString())
        resValue("string", "wakatime_profile_base_url", extra["WAKATIME_PROFILE_BASE_URL"].toString())

        resValue("string", "oauth_client_id", extra["OAUTH_CLIENT_ID"].toString())
        resValue("string", "oauth_client_secret", extra["OAUTH_CLIENT_SECRET"].toString())
        resValue("string", "oauth_redirect_url", extra["OAUTH_REDIRECT_URL"].toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }

    testFixtures {
        enable = true
    }
}

dependencies {
    implementation(libs.security.crypto)
    implementation(libs.decompose)
    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.rx)
    implementation(libs.ktor.client.core)

    implementation(libs.koin)

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.performance)

    implementation(libs.activity.compose)
    implementation(libs.material)

    testImplementation(libs.junit)
    testFixturesImplementation(libs.junit)
    testFixturesImplementation(libs.mockito.core)
    testFixturesImplementation(libs.mockito.kotlin)
    testFixturesImplementation(libs.koin.test)
    testFixturesImplementation(libs.koin.test.junit4)
    testFixturesImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(libs.androidx.test.ext.junit)
}
