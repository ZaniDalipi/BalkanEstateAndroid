plugins {
    id("balkanEstateAndroid.library.compose")
}

android {
    namespace = "com.zanoapps.core.presentation.designsystem"

}

dependencies {

    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.compose.ui)
    implementation (libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.material3)
    debugImplementation (libs.androidx.compose.ui.tooling)
    api (libs.androidx.compose.material3)

    implementation(libs.coil.compose)
    implementation(projects.core.domain)
}