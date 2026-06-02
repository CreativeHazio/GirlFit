plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidKMPLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.android.lint)
}

kotlin {

    android {
        namespace = "com.creativehazio.designsystem"
        compileSdk {
            version = release(36) {
                minorApiLevel = 1
            }
        }
        minSdk = 29

        withHostTestBuilder {
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }

    val xcfName = "core:designsystemKit"

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                api(libs.compose.runtime)
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.ui)
                api(libs.compose.uiToolingPreview)
                implementation(libs.compose.components.resources)

                api(libs.coil.compose)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.compose.uiTooling)
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
                implementation(libs.androidx.runner)
                implementation(libs.androidx.core)
                implementation(libs.androidx.testExt.junit)
            }
        }

        iosMain {
            dependencies {

            }
        }
    }

}

compose.resources {
    publicResClass = true
    packageOfResClass = "girlfit.core.designsystem.generated.resources"
    generateResClass = always
}
