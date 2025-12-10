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
        gradlePluginPortal()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        }
        // Repositorio de Mapbox con autenticación
        //maven {
        //    url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
        //    authentication {
        //        create("basic", org.gradle.authentication.http.BasicAuthentication::class.java)
        //    }

        //    credentials {
        //        username = "mapbox"
        //        password = providers.gradleProperty("MAPBOX_DOWNLOADS_TOKEN").orNull
        //    }
        //}
    }
}

rootProject.name = "Productos"
include(":app")
