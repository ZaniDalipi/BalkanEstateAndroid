plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library)
    alias(libs.plugins.balkanEstateAndroid.jvm.ktor)
}

android {
    namespace = "com.zanoapps.profile.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.profile.domain)

    // Koin DI
    implementation(libs.bundles.koin)

    // DataStore for settings
    implementation(libs.androidx.datastore.preferences)
}
