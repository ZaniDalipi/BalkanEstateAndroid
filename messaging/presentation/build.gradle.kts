plugins {
    alias(libs.plugins.balkanEstateAndroid.android.feature.ui)
}

android {
    namespace = "com.zanoapps.messaging.presentation"

}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.messaging.domain)
}