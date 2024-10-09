/*
 *  Copyright (C) 2022-2024. Maximilian Keppeler (https://www.maxkeppeler.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.multiplatform)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "${JavaVersion.VERSION_11}"
                freeCompilerArgs += "-Xjdk-release=${JavaVersion.VERSION_11}"
            }
        }
    }
}

android {
    namespace = App.ID
    defaultConfig {
        applicationId = App.ID
        compileSdk = App.COMPILE_SDK
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        versionCode = App.VERSION_CODE
        versionName = App.VERSION_NAME
        testInstrumentationRunner = App.TEST_INSTRUMENTATION_RUNNER
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
        // Flag to enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Modules

    Modules.values().forEach { module ->
        api(project(module.path))
    }

    // Dependencies of sheets-compose-dialogs
//
//    val sheetsVersion = "1.1.1"
////
//    implementation("com.maxkeppeler.sheets-compose-dialogs:core:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:info:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:color:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:calendar:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:clock:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:duration:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:date-time:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:option:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:list:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:input:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:state:$sheetsVersion")
//    implementation("com.maxkeppeler.sheets-compose-dialogs:emoji:$sheetsVersion")


    coreLibraryDesugaring(libs.desugar)


    // AndroidX libs
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)


    // Compose libs

    // Test libs
    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(project(":test"))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(libs.junit)
}