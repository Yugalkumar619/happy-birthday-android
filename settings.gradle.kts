pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.google.com")
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/jcenter")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.google.com")
        maven("https://jitpack.io")
        maven("https://maven.aliyun.com/repository/jcenter")
    }
}

rootProject.name = "happy_birthday"
include(":app")




