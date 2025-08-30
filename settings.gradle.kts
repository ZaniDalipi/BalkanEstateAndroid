pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "BalkanEstateAndroid"
include(":app")
include(":core:data")
include(":core:domain")
include(":core:presentation")
include(":core:presentation:designSystem")
include(":core:presentation:ui")
include(":ads:data")
include(":ads:domain")
include(":ads:presentation")
include(":search:data")
include(":search:domain")
include(":search:presentation")
include(":agent:data")
include(":agent:domain")
include(":agent:presentation")
include(":profile:data")
include(":profile:domain")
include(":profile:presentation")
include(":favourites:data")
include(":favourites:domain")
include(":favourites:presentation")
include(":messaging:data")
include(":messaging:domain")
include(":messaging:presentation")
include(":notification:data")
include(":notification:domain")
include(":notification:presentation")
include(":map:data")
include(":map:domain")
include(":map:presentation")
include(":property_details:data")
include(":property_details:domain")
include(":property_details:presentation")
include(":media:data")
include(":media:domain")
include(":media:presentation")

include(":core:database")
include(":auth:data")
include(":auth:domain")
include(":auth:presentation")
