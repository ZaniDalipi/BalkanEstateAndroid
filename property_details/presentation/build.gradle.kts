plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.property_details.presentation"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.propertyDetails.domain)

    // Media3 ExoPlayer for video playback
    implementation(libs.bundles.media3)

    // Coil for image loading
    implementation(libs.coil.compose)
}