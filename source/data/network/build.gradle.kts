plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.realm)
}

android {
    namespace = "com.voxeldev.canoe.network"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    packaging {
        resources {
            excludes += "/META-INF/INDEX.LIST"
        }
    }

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
    implementation(libs.koin)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.realm)

    implementation(project(":source:feature:dashboard-api"))
    implementation(project(":source:feature:leaderboards-api"))
    implementation(project(":source:feature:projects-api"))
    implementation(project(":source:feature:settings-api"))
    implementation(project(":source:data:database"))
    implementation(project(":source:utils"))
    implementation(project(":source:data:local"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
