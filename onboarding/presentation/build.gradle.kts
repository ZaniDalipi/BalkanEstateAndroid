plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}
android {
    namespace = "com.zanoapps.onboarding.presentation"

}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.onboarding.domain)

}
