import com.android.build.api.dsl.ApplicationExtension
import com.zanoapps.convention.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType


class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {

            pluginManager.apply("balkanEstateAndroid.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extensions = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extensions)
        }

    }
}