plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.property_details.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.ui)
    implementation(projects.propertyDetails.domain)

    implementation(libs.coil.compose)
}