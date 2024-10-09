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
    namespace = "com.maxkeppeler.sheets.test"
    defaultConfig {
        compileSdk = App.COMPILE_SDK
        minSdk = App.MIN_SDK
        targetSdk = App.TARGET_SDK
        testInstrumentationRunner = App.TEST_INSTRUMENTATION_RUNNER
        testApplicationId = "com.maxkeppeler.sheets.test"
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
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
    api(project(Modules.CORE.path))
    implementation(libs.androidx.ui.test.junit4)
}