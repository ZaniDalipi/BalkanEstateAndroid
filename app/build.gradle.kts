plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "com.zanoapps.balkanestateandroid"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.zanoapps.balkanestateandroid"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // timber
    implementation(libs.timber)


    // all the app modules included here because this is the place where everything is glued together
    dependencies {
        // Core modules
        implementation(projects.core.data)
        implementation(projects.core.domain)
        implementation(projects.core.presentation)
        implementation(projects.core.presentation.designSystem)
        implementation(projects.core.presentation.ui)

        // Ads modules
        implementation(projects.ads.data)
        implementation(projects.ads.domain)
        implementation(projects.ads.presentation)

        // Search modules
        implementation(projects.search.data)
        implementation(projects.search.domain)
        implementation(projects.search.presentation)

        // Agent modules
        implementation(projects.agent.data)
        implementation(projects.agent.domain)
        implementation(projects.agent.presentation)

        // Profile modules
        implementation(projects.profile.data)
        implementation(projects.profile.domain)
        implementation(projects.profile.presentation)

        // Favourites modules
        implementation(projects.favourites.data)
        implementation(projects.favourites.domain)
        implementation(projects.favourites.presentation)

        // Messaging modules
        implementation(projects.messaging.data)
        implementation(projects.messaging.domain)
        implementation(projects.messaging.presentation)

        // Notification modules
        implementation(projects.notification.data)
        implementation(projects.notification.domain)
        implementation(projects.notification.presentation)

        // Map modules
        implementation(projects.map.data)
        implementation(projects.map.domain)
        implementation(projects.map.presentation)

        // Property Details modules
        implementation(projects.propertyDetails.data)
        implementation(projects.propertyDetails.domain)
        implementation(projects.propertyDetails.presentation)

        // Media modules
        implementation(projects.media.data)
        implementation(projects.media.domain)
        implementation(projects.media.presentation)
    }

}