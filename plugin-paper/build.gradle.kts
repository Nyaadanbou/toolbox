import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription.RelativeLoadOrder

plugins {
    id("toolbox-conventions.commons")
    id("nyaadanbou-conventions.repositories")
    id("nyaadanbou-conventions.copy-jar")
    alias(libs.plugins.pluginyml.paper)
}

group = "cc.mewcraft.toolbox"
version = "1.0.0-SNAPSHOT"

dependencies {
    // server
    compileOnly(local.paper)
    compileOnly(local.helper)

    implementation(platform(libs.bom.cloud.paper))
    implementation(platform(libs.bom.cloud.kotlin)) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }
}

tasks {
    copyJar {
        environment = "paper"
        jarFileName = "Toolbox-${project.version}.jar"
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
    }
}