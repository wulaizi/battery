pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.aliyun.com/repository/public")
        maven {
            credentials {
                username = "611cb37ccd146a5e9cc23b6e"
                password = "J7IZlu4Xge3S"
            }
            setUrl("https://packages.aliyun.com/60e5087c2f222be2d940156b/maven/2119021-release-oxlyuf")
        }
        gradlePluginPortal()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://maven.aliyun.com/repository/public")
        maven {
            credentials {
                username = "611cb37ccd146a5e9cc23b6e"
                password = "J7IZlu4Xge3S"
            }
            setUrl("https://packages.aliyun.com/60e5087c2f222be2d940156b/maven/2119021-release-oxlyuf")
        }
    }
}


rootProject.name = "MlKitLib"
include(":app")
include(":mlkitlibrary")
