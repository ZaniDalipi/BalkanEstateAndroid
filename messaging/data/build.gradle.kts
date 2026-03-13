plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library)
    alias(libs.plugins.balkanEstateAndroid.jvm.ktor)
}

android {
    namespace = "com.zanoapps.messaging.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.messaging.domain)
}
