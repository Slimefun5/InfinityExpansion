plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.3"
}

fun latestGitTagVersion(): String? = try {
    val out = providers.exec { workingDir = rootDir; commandLine("git","describe","--tags","--abbrev=0"); isIgnoreExitValue = true }
    if (out.result.get().exitValue == 0) out.standardOutput.asText.get().trim().removePrefix("gh-").removePrefix("v").takeIf { it.isNotBlank() } else null
} catch (e: Exception) { null }

group = "io.github.mooy1"
version = (project.findProperty("artifact_version") as String?)?.removePrefix("v")?.takeIf { it.isNotBlank() } ?: latestGitTagVersion() ?: "1.0.0"
val versionSuffix: String = when {
    !(project.findProperty("artifact_version") as String?).isNullOrBlank() -> ""
    System.getenv("GITHUB_ACTIONS") == "true" -> "-EXPERIMENTAL"
    else -> "-UNOFFICIAL"
}
val displayVersion = "${project.version}$versionSuffix"
description = "InfinityExpansion is a Slimefun addon that adds machines, generators, and more endgame content."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-public/")
}

dependencies {
<<<<<<< HEAD
    implementation("com.github.Slimefun5:SlimefunMetrics:master-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
=======
    githubCompileOnly("Slimefun5:Slimefun5:gh-v5.2.3.2")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
>>>>>>> origin/experimental
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    // Shaded
    githubImplementation("Slimefun5:InfinityLib:v1.3.13")

}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to displayVersion)
        }
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("InfinityExpansion-$displayVersion.jar")
        relocate("io.github.mooy1.infinitylib", "io.github.mooy1.infinityexpansion.infinitylib")
        minimize()
        exclude("META-INF/**")
        // Core is provided at runtime (depend: Slimefun); never bundle it. github-gradle pulls it in
        // transitively via InfinityLib's metadata, so exclude it explicitly.
        exclude("io/github/thebusybiscuit/slimefun5/**")
    }
    build {
        dependsOn(shadowJar)
    }
    compileTestJava {
        enabled = false
    }
    test {
        enabled = false
    }
}
