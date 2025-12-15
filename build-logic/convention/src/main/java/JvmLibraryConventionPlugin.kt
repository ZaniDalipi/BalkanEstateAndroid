import com.android.build.api.dsl.ApplicationExtension
import com.zanoapps.convention.ExtensionType
import com.zanoapps.convention.configBuildTypes
import com.zanoapps.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure


class JvmLibraryConventionPlugin : Plugin<Project> {

    override fun apply(projectTarget: Project) {
        projectTarget.run {
            // applying the plugins that we want to together with this convention plugin
            pluginManager.run {
                apply("org.jetbrains.kotlin.jvm")
                configureKotlinJvm()
            }
            dependencies.add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            dependencies.add("implementation", "org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.7.3")
        }
    }
}