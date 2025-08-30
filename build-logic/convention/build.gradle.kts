import org.gradle.kotlin.dsl.implementation

plugins {
    `kotlin-dsl`
}

group = "com.zanoapps.balkanestateandroid.buildlogic"

dependencies {

    implementation(libs.serialization)

    // includes this only during compile time
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)

//    compileOnly(libs.sqldelight.gradlePlugin)


}

gradlePlugin {

    plugins {
        register("androidApplication") {
            id = "balkanEstateAndroid.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }


        register("androidApplicationCompose") {
            id = "balkanEstateAndroid.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "balkanEstateAndroid.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "balkanEstateAndroid.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }


        register("androidFeatureUi") {
            id = "balkanEstateAndroid.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }

        register("androidRoom") {
            id = "balkanEstateAndroid.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("androidDynamicFeature") {
            id = "balkanEstateAndroid.android.dynamic.feature"
            implementationClass = "AndroidDynamicFeatureConventionPlugin"
        }
        register("jvmLibray") {
            id = "balkanEstateAndroid.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("jvmKtor") {
            id = "balkanEstateAndroid.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }
    }
}