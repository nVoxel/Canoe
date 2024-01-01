plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.voxeldev.canoe.root"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

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
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.decompose.extensions)
    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.main)
    implementation(libs.essenty.lifecycle)
    implementation(libs.kotlinx.serialization.json)

    api(project(":source:feature:dashboard"))
    api(project(":source:feature:projects"))
    api(project(":source:feature:settings"))
    api(project(":source:feature:settings-api"))
    api(project(":source:feature:leaderboards"))
    api(project(":source:utils"))

    implementation(libs.koin)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
