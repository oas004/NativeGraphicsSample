pluginManagement {
    listOf(repositories, dependencyResolutionManagement.repositories).forEach {
        it.apply {
            // For Apollo Kotlin snapshots
            maven {
                url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            }
            mavenCentral()
            google()
            maven("https://androidx.dev/storage/compose-compiler/repository")
            gradlePluginPortal {
                content {
                }
            }
            maven {
                url = uri("https://jitpack.io")
                content {
                    includeGroup("com.github.QuickBirdEng.kotlin-snapshot-testing")
                }
            }
        }
    }
}
rootProject.name = "NativeGraphicsSample"
include(":app")
