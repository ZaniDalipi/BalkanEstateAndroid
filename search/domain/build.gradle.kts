plugins {
    alias(libs.plugins.balkanEstateAndroid.jvm.library)
}

dependencies {
    implementation(projects.core.domain)

    // Coroutines for Flow
    implementation(libs.kotlinx.coroutines.core)
}