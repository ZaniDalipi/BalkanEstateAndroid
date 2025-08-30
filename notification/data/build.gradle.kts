plugins {
    alias(libs.plugins.balkanEstateAndroid.jvm.library)
//    alias(libs.plugins.balkanEstateAndroid.jvm.ktor)
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.notification.domain)
}
