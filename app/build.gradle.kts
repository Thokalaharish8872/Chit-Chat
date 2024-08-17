plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt") // Add this line for Kotlin annotation processing
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.neatrootslearning"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.neatrootslearning"
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.android.car.ui:car-ui-lib:2.6.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation("com.google.android.gms:play-services-ads-lite:23.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.activity:activity:1.8.0")
    implementation("com.google.android.things:androidthings:1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.gms:play-services-ads:23.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("io.github.itsxtt:pattern-lock:0.2.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.8.0")
    implementation("com.spotify.android:auth:1.2.3")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.google.firebase:firebase-messaging:23.0.0")
    implementation ("io.github.webrtc-sdk:android:125.6422.03")
//    implementation("com.google.firebase:firebase-database:20.0.5")



    // Ensure no duplicates or unnecessary dependencies
}

// Apply the Google Services plugin (if needed)
apply(plugin = "com.google.gms.google-services")
