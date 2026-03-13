plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.auth.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.ads.domain)
    implementation(projects.auth.domain)

    implementation(libs.coil.compose)

}