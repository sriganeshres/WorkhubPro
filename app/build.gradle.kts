plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.work.workhubpro"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.work.workhubpro"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"

    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
kapt {
    correctErrorTypes = true
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
//    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("io.coil-kt:coil-compose:2.2.2")
    implementation(libs.androidx.ui.tooling.preview)
    implementation ("io.ktor:ktor-client-android:1.6.4")
    implementation("io.ktor:ktor-client-websockets:1.6.4")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation("androidx.compose.material3:material3:1.0.0-alpha14")
//    implementation ("androidx.compose.ui:ui-image:1.2.0-beta01")
//    implementation ("androidx.compose.foundation:foundation-image:1.3.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:1.0.0")
//    implementation(libs.androidx.material3.android)
//    implementation ("androidx.compose.material3:material3:$m3-version")
//    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.ktx)
    // For hilt Implementation
    implementation ("com.google.dagger:hilt-android:2.49")
//    implementation(libs.androidx.constraintlayout)
    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")

    kapt ("com.google.dagger:hilt-compiler:2.46.1")

    kapt("androidx.hilt:hilt-compiler:1.2.0")

    // For instrumentation tests
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.46.1")
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.46.1")

    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.46.1")
    kaptTest ("com.google.dagger:hilt-compiler:2.46.1")
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}