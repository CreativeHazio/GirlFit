rootProject.name = "GirlFit"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":androidApp")
include(":core:designsystem")
include(":core:common")
include(":core:database")
include(":feature:auth")
include(":feature:home")
include(":feature:workout")
include(":feature:progress")
include(":feature:meals")
include(":feature:me")
include(":feature:fitnessbuddy")
include(":core:navigation")
