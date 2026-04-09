pluginManagement {
    repositories {
        google()               // VERY IMPORTANT
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()               // VERY IMPORTANT
        mavenCentral()
    }
}

rootProject.name = "RationManProject"
include(":app")