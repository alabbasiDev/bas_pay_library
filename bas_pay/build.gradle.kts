import org.jetbrains.kotlin.gradle.dsl.JvmTarget

import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)

    alias(libs.plugins.vanniktech.mavenPublish)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxSerialization)
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_1_8)
                }
            }
        }
    }
    
    val xcf = XCFramework()
    listOf(
        /// For Intel Macs
//        iosX64(),
        /// iOS Device Target
        iosArm64("iosDeviceArm64"),
        /// iOS Simulator Targets ... And For Apple Silicon Macs (M1, M2, M3...)
//        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "bas_pay"
            xcf.add(this)
            isStatic = true
            transitiveExport = false
            freeCompilerArgs += listOf(
               "-opt",
               "-Xllvm-lto-level=thin",
               "-Xlinker",
            )
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.webview.multiplatform)
            implementation(compose.runtime)
            implementation(compose.foundation)
//            implementation(compose.material3)
//            implementation(compose.ui)
//            implementation(libs.androidx.compose.bom)
            implementation(libs.kotlinx.serialization.json)
            //put your multiplatform dependencies here
        }

        androidMain.dependencies {
            compileOnly(files("src/androidMain/libs/BankySDKManager-release.aar"))
//            implementation("com.android.support:appcompat-v7:28.0.0")
//            implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
//            implementation ("com.squareup.okhttp3:okhttp:4.8.0")
        }

    }
}

android {
    namespace = "com.superstore.bas_pay"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
//        setProperty("archivesBaseName", "osama")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


}


/// commands

// ./gradlew build
// ./gradlew clean
// ./gradlew --stop
// ./gradlew build release
// ./gradlew :bas_pay:packForXcode
// ./gradlew :shared:assembleXCFramework

/// build by this for ios
//
// ./gradlew :bas_pay:assembleXCFramework
// ./gradlew :bas_pay:linkReleaseFrameworkIosArm64

// ./gradlew :bas_pay:assembleReleaseXCFramework
// ./gradlew :shared:assembleSharedModuleXCFramework
//# Or for a specific build type:
//# ./gradlew :shared:assembleSharedModuleDebugXCFramework
//# ./gradlew :shared:assembleSharedModuleReleaseXCFramework
// ./gradlew clean build

