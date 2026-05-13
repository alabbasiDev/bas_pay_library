enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
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

rootProject.name = "bas_pay_library"
include(":bas_pay")