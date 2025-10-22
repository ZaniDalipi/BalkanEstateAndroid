plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library.compose)
}

android {
    namespace = "com.zanoapps.core.presentation"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.graphics)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)

    implementation(projects.core.domain)

}