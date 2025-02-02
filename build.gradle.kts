import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "8.10"
    id("com.gradleup.shadow") version "8.3.0"
}

object Project {
    const val NAME = "MenuAPI"
    const val GROUP = "net.j4c0b3y"
    const val AUTHOR = "J4C0B3Y"
    const val VERSION = "1.4.2"
}

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/nms/")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

tasks {
    register<Copy>("copy") {
        from(named("shadowJar"))
        rename("(.*)-all.jar", "${Project.NAME}-${Project.VERSION}.jar")
        into(file("jars"))
    }

    register("delete") {
        file("jars").deleteRecursively()
    }

    register("install") {
        dependsOn(named("publishReleasePublicationToMavenLocal"))
    }

    named<JavaCompile>("compileJava") {
        options.encoding = "UTF-8"
    }
}

publishing {
    repositories {
        maven {
            name = "j4c0b3yPublic"
            url = uri("https://repo.j4c0b3y.net/public")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("release") {
            artifactId = Project.NAME
            groupId = Project.GROUP
            version = Project.VERSION

            artifact(tasks.named<ShadowJar>("shadowJar").get().archiveFile)

            artifact(tasks.named<Jar>("sourcesJar").get().archiveFile) {
                classifier = "sources"
            }
        }
    }
}
