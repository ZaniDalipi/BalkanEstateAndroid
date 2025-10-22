plugins {

    alias(libs.plugins.balkanEstateAndroid.jvm.library)
}
dependencies {
    implementation(projects.core.domain)
}