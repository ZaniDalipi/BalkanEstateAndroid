plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library)
    alias(libs.plugins.balkanEstateAndroid.jvm.ktor)
}
android {
    namespace = "com.zanoapps.map.data"
}
dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.map.domain)
}
