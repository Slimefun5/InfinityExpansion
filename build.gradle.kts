plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.3"
}

group = "io.github.mooy1"
description = "InfinityExpansion is a Slimefun addon that adds machines, generators, and more endgame content."

apply(from = "https://raw.githubusercontent.com/Slimefun5/gradle/stable/slimefun-addon.gradle")

dependencies {
<<<<<<< HEAD
<<<<<<< HEAD
    implementation("com.github.Slimefun5:SlimefunMetrics:master-SNAPSHOT")
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
=======
    githubCompileOnly("Slimefun5:Slimefun5:gh-v5.2.3.2")
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
>>>>>>> origin/experimental
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")

    // Shaded
=======
>>>>>>> origin/experimental
    githubImplementation("Slimefun5:InfinityLib:v1.3.13")
}

tasks {
    shadowJar {
        relocate("io.github.mooy1.infinitylib", "io.github.mooy1.infinityexpansion.infinitylib")
        minimize()
        exclude("io/github/thebusybiscuit/slimefun5/**")
    }
}
