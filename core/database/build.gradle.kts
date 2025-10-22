plugins {
    alias(libs.plugins.balkanEstateAndroid.android.library)
    alias(libs.plugins.balkanEstateAndroid.android.room)
}

android {
    namespace = "com.zanoapps.core.database"
}

dependencies {

    implementation(libs.org.mongodb.bson)
    implementation(projects.core.domain)
}