import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription.*

plugins {
    id("nyaadanbou-conventions.repositories")
    id("nyaadanbou-conventions.copy-jar")
    id("toolbox-conventions.commons")
    alias(libs.plugins.pluginyml.paper)
}

group = "cc.mewcraft.toolbox"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":common", configuration = "shadow"))
    implementation(local.commons.collections)
    implementation(local.commons.provider)
    implementation(platform(libs.bom.configurate.yaml))
    implementation(platform(libs.bom.configurate.kotlin))
    implementation(platform(libs.bom.cloud.paper))
    implementation(platform(libs.bom.cloud.kotlin)) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }
    implementation(platform(local.koin.bom))
    implementation(local.koin.core) {
        exclude("org.jetbrains.kotlin")
    }
    implementation(local.koin.core.coroutines) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }
    implementation(platform(libs.bom.packetevents.spigot))

    testImplementation(local.koin.test) { exclude("org.jetbrains.kotlin") }
    testImplementation(local.koin.test.junit5) { exclude("org.jetbrains.kotlin") }

    compileOnly(local.paper)
    compileOnly(local.helper)
    compileOnly(libs.essentials) {
        exclude("io.papermc")
        exclude("org.spigotmc")
    }
}

tasks {
    shadowJar {
        relocate("org.spongepowered.configurate", "cc.mewcraft.wakame.external.config")
    }
    copyJar {
        environment = "paper"
        jarFileName = "toolbox-${project.version}.jar"
    }
}

paper {
    main = "cc.mewcraft.toolbox.ToolboxPlugin"
    name = "Toolbox"
    version = "${project.version}"
    description = project.description
    apiVersion = "1.21"
    author = "g2213swo"
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    serverDependencies {
        register("helper") {
            required = true
            load = RelativeLoadOrder.BEFORE
        }
        register("Essentials") {
            required = false
            load = RelativeLoadOrder.OMIT
        }
    }
}