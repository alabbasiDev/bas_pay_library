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
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "BasPay"
            xcf.add(this)
            isStatic = false
//            isStatic = true
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
// ./gradlew :shared:assembleXCFramework
// ./gradlew :bas_pay:assembleXCFramework
// ./gradlew :bas_pay:assembleReleaseXCFramework
// ./gradlew :shared:assembleSharedModuleXCFramework
//# Or for a specific build type:
//# ./gradlew :shared:assembleSharedModuleDebugXCFramework
//# ./gradlew :shared:assembleSharedModuleReleaseXCFramework
// ./gradlew clean build