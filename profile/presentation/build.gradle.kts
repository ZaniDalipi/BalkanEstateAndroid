plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.profile.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.ui)
    implementation(projects.profile.domain)

    implementation(libs.coil.compose)
}