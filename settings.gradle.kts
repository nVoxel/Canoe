pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Canoe"
include(":canoe")
include(":source:compose-ui")
include(":source:data:database")
include(":source:data:local")
include(":source:data:network")
include(":source:feature:dashboard")
include(":source:feature:dashboard-api")
include(":source:feature:leaderboards")
include(":source:feature:leaderboards-api")
include(":source:feature:projects")
include(":source:feature:projects-api")
include(":source:feature:settings")
include(":source:feature:settings-api")
include(":source:root")
include(":source:utils")
