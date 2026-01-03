plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.zanoapps.onboarding.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.onboarding.domain)

    // Koin DI
    implementation(libs.bundles.koin)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
}
