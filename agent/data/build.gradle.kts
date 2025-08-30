plugins {
    alias(libs.plugins.balkanEstateAndroid.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.agent.domain)
}
