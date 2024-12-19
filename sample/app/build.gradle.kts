import java.io.ByteArrayOutputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.pulseinsights.surveysdkexample"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.pulseinsights.surveysdkexample"
        minSdk = 23
        targetSdk = 33
        versionCode = getVersionCode()
        versionName = "3.1.2"

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    flavorDimensions += "version"
    productFlavors {
        create("stag") {
            dimension = "version"
            applicationIdSuffix = ".stag"
        }
        create("prod") {
            dimension = "version"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //    Switch-enable the following two lines when test-deployed-sdk/test-local
//    implementation("com.pulseinsights:android-sdk:2.4.1")
    implementation(project(mapOf("path" to ":surveysdk")))
}

fun getVersionCode(): Int {
    val output = ByteArrayOutputStream()
    exec {
        commandLine = listOf("git", "rev-list", "--count", "HEAD")
        standardOutput = output
    }
    return output.toString().trim().toInt()
}