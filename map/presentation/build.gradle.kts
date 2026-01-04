plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.map.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.ui)
    implementation(projects.map.domain)

    implementation(libs.coil.compose)

    // Google Maps
    implementation(libs.google.maps.android.compose)
    implementation(libs.play.services.maps)

}