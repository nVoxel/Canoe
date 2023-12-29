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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
}

dependencies {
    implementation(libs.security.crypto)
    implementation(libs.decompose)
    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.rx)
    implementation(libs.ktor.client.core)

    implementation(libs.koin)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
