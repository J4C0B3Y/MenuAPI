import java.nio.charset.StandardCharsets

plugins {
    java
    `maven-publish`
    id("io.freefair.lombok") version "8.12.1"
}

object Project {
    const val NAME = "MenuAPI"
    const val GROUP = "net.j4c0b3y"
    const val VERSION = "1.5.0"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")
    apply(plugin = "maven-publish")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
    }

    tasks {
        compileJava {
            options.encoding = StandardCharsets.UTF_8.name()
        }
    }

    publishing {
        repositories {
            maven("https://repo.j4c0b3y.net/public/") {
                name = "j4c0b3yPublic"

                credentials(PasswordCredentials::class)

                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }

        publications {
            create<MavenPublication>(name) {
                artifactId = Project.NAME + "-" + name
                groupId = Project.GROUP
                version = Project.VERSION

                from(components["java"])
            }
        }
    }
}


