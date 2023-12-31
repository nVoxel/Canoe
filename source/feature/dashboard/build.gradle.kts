plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.voxeldev.canoe.dashboard"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.get()
    }
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.decompose.extensions)
    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.extensions.coroutines)

    implementation(project(":source:data:network"))
    implementation(project(":source:utils"))
    api(project(":source:feature:dashboard-api"))

    implementation(libs.koin)

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.performance)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
