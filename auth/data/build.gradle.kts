plugins {
    alias(libs.plugins.balkanEstateAndroid.jvm.library)
    // maybe if i need ktor
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.auth.domain)
}