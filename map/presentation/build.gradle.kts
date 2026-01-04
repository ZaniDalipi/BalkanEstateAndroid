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

    // Leaflet via WebView (free, no API key required)
    // OSMDroid for native OpenStreetMap support
    implementation(libs.osmdroid)

    // Location services (still needed for user location)
    implementation(libs.play.services.location)

    // Coil for images
    implementation(libs.coil.compose)
}
