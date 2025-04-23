plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.examproject"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.examproject"
        minSdk = 26
        targetSdk = 34
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.gridlayout)
    implementation(libs.firebase.auth)
    val fragment_version = "1.8.3"
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.fragment:fragment:$fragment_version")
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth");
        // Face features
    implementation("com.google.mlkit:face-detection:16.0.0");
        // Text features
    implementation("com.google.android.gms:play-services-mlkit-text-recognition:16.0.0");
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.0");
    implementation("androidx.navigation:navigation-ui-ktx:2.7.0");
}