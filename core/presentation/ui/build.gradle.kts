plugins {
    id("balkanEstateAndroid.library.compose")
}

android {
    namespace = "com.zanoapps.presentation.ui"

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}