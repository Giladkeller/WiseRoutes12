plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.wiseroute12"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
        }
    defaultConfig {
        applicationId = "com.example.wiseroute12"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        // API KEY מתוך local.properties
        buildConfigField(
            "String",
            "OPENAI_API_KEY",
            "\"${System.getenv("OPENAI_API_KEY") ?: project.findProperty("OPENAI_API_KEY")}\""
        )

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


}

dependencies {
    // Android בסיסי
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // OkHttp (חובה ל־API)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
