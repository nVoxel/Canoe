plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)

    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.voxeldev.canoe"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.voxeldev.canoe"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.version.code.get().toInt()
        versionName = libs.versions.version.name.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            splits {
                abi {
                    isEnable = true
                    reset()
                    include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
                    isUniversalApk = true
                }
            }
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":source:root"))
    implementation(project(":source:compose-ui"))
    implementation(project(":source:feature:dashboard"))
    implementation(project(":source:feature:leaderboards"))
    implementation(project(":source:feature:projects"))
    implementation(project(":source:feature:settings"))
    implementation(project(":source:feature:settings-api"))
    implementation(project(":source:utils"))

    implementation(libs.decompose)

    implementation(libs.mvikotlin)
    implementation(libs.mvikotlin.main)

    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.serialization)

    implementation(libs.koin)

    implementation(libs.browser)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))

    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.performance)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(platform(libs.compose.bom))

    testImplementation(libs.junit)

    debugRuntimeOnly(libs.ui.test.manifest)
}
