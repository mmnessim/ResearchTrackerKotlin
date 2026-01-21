import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    dependencies {
        implementation(projects.composeApp)
        implementation(compose.preview)
        implementation(libs.androidx.activity.compose)
        implementation(libs.sqldelight.android)
        implementation(libs.koin.android)
        implementation(libs.koin.compose)
        implementation(libs.ktor.client.okhttp)
        implementation(libs.androidx.work.runtime)
    }

    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

}

android {
    namespace = "com.mnessim.rsstracker"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.mnessim.rsstracker"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 10
        versionName = "1.0.10"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}