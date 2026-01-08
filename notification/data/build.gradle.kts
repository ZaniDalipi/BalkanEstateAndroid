plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library)
    alias(libs.plugins.balkanEstateAndroid.jvm.ktor)
    alias(libs.plugins.balkanEstateAndroid.android.room)
}
android {
    namespace = "com.zanoapps.notification.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.notification.domain)

    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Firebase Messaging
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // Koin for dependency injection in service
    implementation(libs.koin.android)

    // Timber for logging
    implementation(libs.timber)
}
