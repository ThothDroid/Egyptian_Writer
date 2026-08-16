import jdk.jfr.internal.JVM.exclude

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.blueapps.egyptianwriter"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.blueapps.egyptianwriter.feature_vocab_trainer"
        minSdk = 23
        targetSdk = 37
        versionCode = 10
        versionName = "17.02.2026@0.1.0@feature_vocab_trainer"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    buildFeatures {
        viewBinding = true
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

tasks.register("testClasses")

dependencies {
    implementation(libs.signprovider)
    implementation(libs.documentfile)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.gridlayout)
    implementation(libs.commons.lang)

    implementation(libs.thoth)
    implementation(libs.maat)

    implementation(libs.android.keyboardlistener) {
        // This library pulls old support libraries (com.android.support:appcompat-v7:26.0.0-alpha1)
        // Exclude the legacy support group so we don't get duplicate android.support.* classes
        exclude(group = "com.android.support")
    }
    implementation(libs.glyphconverter)
    implementation(libs.expandable.layout)
    implementation(libs.recyclerview)
    implementation(libs.fragment)
    implementation(libs.viewpager2)
    implementation(libs.cardview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}