plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.agent.presentation"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.presentation.ui)
    implementation(projects.agent.domain)

    implementation(libs.coil.compose)
}