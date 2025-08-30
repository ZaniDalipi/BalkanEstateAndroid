package com.zanoapps.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configBuildTypes(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    extensionType: ExtensionType,
) {
    commonExtension.run {

        buildFeatures {
            buildConfig = true
        }



        when (extensionType) {
            ExtensionType.APPLICATION -> {
                extensions.configure<ApplicationExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()

                        }
                        create("staging") {
                            configureStagingBuildType()
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension
                            )
                        }
                    }
                }


            }

            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()

                        }
                        create("staging") {
                            configureStagingBuildType()
                        }
                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension
                            )
                        }
                    }
                }
            }

            ExtensionType.DYNAMIC_FEATURE -> {
                extensions.configure<DynamicFeatureExtension> {
                    buildTypes {
                        debug {
                            configureDebugBuildType()

                        }

                        create("staging") {
                            configureStagingBuildType()
                        }

                        release {
                            configureReleaseBuildType(
                                commonExtension = commonExtension
                            )
                            isMinifyEnabled = false
                        }
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType() {
// api key and base urls ( they have to be in local.properties defined)
}

private fun BuildType.configureStagingBuildType() {
// api key and base urls ( they have to be in local.properties defined)
}

private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {

    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}

