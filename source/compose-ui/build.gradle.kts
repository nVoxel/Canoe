plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.voxeldev.canoe.compose.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.extension.get()
    }
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.decompose.extensions)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation.android)
    implementation(libs.ui.tooling.preview)
    implementation(libs.paging.compose)
    implementation(libs.shimmer)

    implementation(libs.material3)
    // implementation(libs.material.icons.extended)

    implementation(libs.coil)
    implementation(libs.vico.compose.m3)
    implementation(libs.ycharts)

    implementation(libs.koin)
    implementation(libs.koin.compose)

    api(project(":source:root"))
    api(project(":source:feature:dashboard"))
    api(project(":source:feature:dashboard-api"))
    api(project(":source:feature:leaderboards"))
    api(project(":source:feature:projects"))
    api(project(":source:feature:settings"))
    implementation(project(":source:feature:leaderboards-api"))
    implementation(project(":source:feature:projects-api"))
    implementation(project(":source:utils"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)

    debugImplementation(libs.ui.tooling)
}
