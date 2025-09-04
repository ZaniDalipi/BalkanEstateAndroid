plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library.compose)
}

android {
    namespace = "com.zanoapps.core.presentation"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(projects.core.domain)

}