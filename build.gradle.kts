plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.3"
}

group = "io.github.mooy1"
description = "InfinityExpansion is a Slimefun addon that adds machines, generators, and more endgame content."

// Shared Slimefun-addon build conventions (Java 8, spigot-api baseline, core dep, publish, shadow, version).
apply(from = "https://raw.githubusercontent.com/Slimefun5/workflows/stable/slimefun-addon.gradle")

dependencies {
    githubImplementation("Slimefun5:InfinityLib:v1.3.13")
}

tasks {
    shadowJar {
        relocate("io.github.mooy1.infinitylib", "io.github.mooy1.infinityexpansion.infinitylib")
        minimize()
        // Core is provided at runtime (depend: Slimefun); never bundle it (InfinityLib pulls it transitively).
        exclude("io/github/thebusybiscuit/slimefun5/**")
    }
    compileTestJava { enabled = false }
    test { enabled = false }
}
