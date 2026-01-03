plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.favourites.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.ui)
    implementation(projects.favourites.domain)

    implementation(libs.coil.compose)
}