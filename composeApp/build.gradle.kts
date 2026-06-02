import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidKMPLibrary)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {

    android {
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        namespace = "com.creativehazio.girlfit"
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            binaryOption("bundleId", "com.creativehazio.girlfit.Composeapp")
        }

    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.ktor.client.android)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {

            implementation(libs.compose.components.resources)

            implementation(libs.coil.network.ktor)
            implementation(libs.ktor.client.core)

            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

            implementation(projects.core.common)
            implementation(projects.core.designsystem)
            implementation(projects.core.navigation)
            implementation(projects.core.data)
            implementation(projects.feature.auth)
            implementation(projects.feature.home)
            implementation(projects.feature.workout)
            implementation(projects.feature.progress)
            implementation(projects.feature.meals)
            implementation(projects.feature.me)
            implementation(projects.feature.fitnessbuddy)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

