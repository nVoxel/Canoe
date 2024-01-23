plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.voxeldev.canoe.projects"
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
}

dependencies {
    implementation(libs.decompose)
    implementation(libs.decompose.extensions)
    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.extensions.coroutines)

    implementation(project(":source:data:network"))
    implementation(project(":source:utils"))
    implementation(project(":source:feature:dashboard"))
    api(project(":source:feature:projects-api"))

    implementation(libs.koin)

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.performance)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.junit)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(testFixtures(project(":source:utils")))

    androidTestImplementation(libs.androidx.test.ext.junit)
}
