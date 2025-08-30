//import app.cash.sqldelight.gradle.SqlDelightExtension
//import com.zanoapps.convention.libs
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
//import org.gradle.kotlin.dsl.configure
//import org.gradle.kotlin.dsl.dependencies
//
//class SqlDelightConventionPlugin : Plugin<Project> {
//    override fun apply(target: Project) {
//        with(target) {
//            pluginManager.apply("app.cash.sqldelight")
//
//            // Configure SQLDelight
//            extensions.configure<SQLDelightExtension>() {
//                database("AppDatabase") {
//                    packageName = "com.example.database" // change to your package
//                    schemaOutputDirectory = file("src/main/sqldelight/databases")
//                    verifyMigrations = true
//                }
//            }
//
//            dependencies {
//                // Core runtime
//                "implementation"(libs.findLibrary("sqldelight-runtime").get())
//                "implementation"(libs.findLibrary("sqldelight.coroutines").get())
//                "implementation"(libs.findLibrary("sqldelight.androidDriver").get())
//                // ios driver here
//            }
//        }
//    }
//}