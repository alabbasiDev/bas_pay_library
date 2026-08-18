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

group = "com.superstore"
version = providers.gradleProperty("VERSION_NAME").orElse("0.0.4").get()

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
//        iosArm64(),
        /// iOS Simulator Targets ... And For Apple Silicon Macs (M1, M2, M3...)
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "bas_pay"
            xcf.add(this)
            isStatic = true
            // CFBundleIdentifier cannot contain underscores (SPM embed validation).
            binaryOption("bundleId", "com.superstore.baspay")
//            transitiveExport = false
//            freeCompilerArgs += listOf(
//               "-opt",
//               "-Xllvm-lto-level=thin",
//               "-Xlinker",
//            )
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.webview.multiplatform)
            implementation(libs.coroutines.core)
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
            implementation("androidx.appcompat:appcompat:1.7.0")
            implementation("androidx.core:core-ktx:1.15.0")
//            implementation("com.android.support:appcompat-v7:28.0.0")
//            implementation ("androidx.localbroadcastmanager:localbroadcastmanager:1.0.0")
//            implementation ("com.squareup.okhttp3:okhttp:4.8.0")
        }

    }
}

mavenPublishing {
    coordinates("com.superstore", "bas-pay")
    pom {
        name.set("BAS Pay Library")
        description.set("BAS Payment SDK for Android")
        url.set("https://github.com/BasPlatform/BasPaymentAndroidSdk")
        licenses {
            license {
                name.set("MIT")
            }
        }
        scm {
            url.set("https://github.com/BasPlatform/BasPaymentAndroidSdk")
            connection.set("scm:git:https://github.com/BasPlatform/BasPaymentAndroidSdk.git")
            developerConnection.set("scm:git:ssh://git@github.com/BasPlatform/BasPaymentAndroidSdk.git")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url =
                uri(
                    "https://maven.pkg.github.com/${
                        providers.gradleProperty("gpr.github.repo").orElse("BasPlatform/BasPaymentAndroidSdk").get()
                    }",
                )
            credentials {
                username =
                    providers.environmentVariable("GITHUB_ACTOR").orNull
                        ?: providers.gradleProperty("gpr.user").orNull
                password =
                    providers.environmentVariable("GITHUB_TOKEN").orNull
                        ?: providers.gradleProperty("gpr.key").orNull
            }
        }
    }
}

android {
    namespace = "com.superstore.bas_pay"
    compileSdk = 36
    defaultConfig {
        minSdk = 24
//        setProperty("archivesBaseName", "alabbasidev")
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
// zip -r bas_pay.xcframework.zip bas_pay.xcframework
// shasum -a 256 bas_pay.xcframework.zip
// ./gradlew :bas_pay:linkReleaseFrameworkIosArm64

// ./gradlew :bas_pay:assembleReleaseXCFramework
// ./gradlew :shared:assembleSharedModuleXCFramework
//# Or for a specific build type:
//# ./gradlew :shared:assembleSharedModuleDebugXCFramework
//# ./gradlew :shared:assembleSharedModuleReleaseXCFramework
// ./gradlew clean build

//  ./gradlew :bas_pay:embedAndSignAppleFrameworkForXcode