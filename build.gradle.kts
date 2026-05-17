plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle")
}

group = "com.github.Mooy1"
version = "v1.0.0-UNOFFICIAL-MC26.1.2"
description = "A Slimefun expansion focused on late-game storage, crafting, and energy"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    "githubCompileOnly"("Slimefun5:Slimefun5:v5.1.1")
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    
    // InfinityLib is now published to mavenLocal
    compileOnly("com.github.Slimefun5:InfinityLib:v1.3.10")
    compileOnly("com.github.Slimefun.dough:dough-api:cb22e71335")
    
    implementation("org.bstats:bstats-bukkit:3.0.2")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    shadowJar {
        relocate("org.bstats", "io.github.mooy1.infinityexpansion.metrics")
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.Mooy1"
            artifactId = "InfinityExpansion"
            version = "d995144"
            artifact(tasks.shadowJar)
        }
    }
}

group = "io.github.mooy1"
version = "1.0.0-UNOFFICIAL"
description = "InfinityExpansion is a Slimefun addon that adds machines, generators, and more endgame content."

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:${property("paperApiVersion")}")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    "githubCompileOnly"("Slimefun5:Slimefun5:v5.1.1")
    compileOnly("com.github.Slimefun.dough:dough-api:cb22e71335")

    implementation("com.github.Slimefun5:InfinityLib:v1.3.10")
    implementation("org.bstats:bstats-bukkit:3.0.2")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.107.0") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

configurations.testImplementation {
    extendsFrom(configurations.compileOnly.get())
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("InfinityExpansion v${project.version}-MC26.1.2.jar")
        relocate("io.github.mooy1.infinitylib", "io.github.mooy1.infinityexpansion.infinitylib")
        relocate("org.bstats", "io.github.mooy1.infinityexpansion.bstats")
        relocate("io.github.bakedlibs.dough", "io.github.thebusybiscuit.slimefun5.libraries.dough")
        minimize()
        exclude("META-INF/**")
    }
    build {
        dependsOn(shadowJar)
    }
    test {
        useJUnitPlatform()
    }
}



// Trigger CI

// Trigger CI again
