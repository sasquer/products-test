import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.sas.productstest"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.sas.productstest"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        debug {

            // Для більш захищеної реалізації можна зберігати такі поля (ключі, налаштування середовищ і т.д.)
            // в [local.properties], тому залишу як варіант реалізації, але цей файл не можна передавати в репозиторій.
            // Тому в вас без нього програма не збілдиться. Для тестового додатка достатньо реалізації нижче
//            val localProperties = Properties().apply {
//                val localPropsFile = rootProject.file("local.properties")
//                if (localPropsFile.exists()) {
//                    load(localPropsFile.inputStream())
//                }
//            }
//            val baseApiUrl = localProperties.getProperty("BASE_API_URL") ?: ""
//            buildConfigField("String", "BASE_API_URL", baseApiUrl)

            buildConfigField("String", "BASE_API_URL", "\"https://fake-store-api.mock.beeceptor.com/api/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragmnet.ktx)

    // Navigation
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.runtime)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)

    // OkHttp
    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logging.interceptor)

    // Hilt
    implementation(libs.google.hilt.android)
    ksp(libs.google.hilt.android.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Glide
    implementation(libs.github.glide)
    ksp(libs.github.glide.compiller)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}