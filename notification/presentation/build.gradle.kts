plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.notification.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.designSystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.notification.domain)

    // Coil for image loading
    implementation(libs.coil.compose)
}