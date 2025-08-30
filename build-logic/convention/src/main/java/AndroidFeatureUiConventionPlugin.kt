import com.zanoapps.convention.addUiLayerDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureUiConventionPlugin: Plugin<Project> {
    override fun apply(project: Project) {
        project.run {
            pluginManager.run {
                apply("balkanEstateAndroid.library.compose")
            }

            dependencies {
                addUiLayerDependencies(project)

            }
        }
    }
}