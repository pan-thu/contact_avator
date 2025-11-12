plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
}

android {
    namespace = "dev.panthu.contactavatorapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.panthu.contactavatorapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Room schema export location
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Activity and Fragment
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)

    // Layout
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.constraintlayout)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // RecyclerView and CardView
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Lifecycle ViewModel and LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Kotlin Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Image Loading - Coil
    implementation("io.coil-kt:coil:2.5.0")

    // Phone Number Validation
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.26")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}