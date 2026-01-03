plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.auth.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.ui)
    implementation(projects.auth.domain)

    implementation(libs.coil.compose)

}