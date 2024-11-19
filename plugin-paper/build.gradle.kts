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

    implementation(platform(libs.bom.configurate.yaml))
    implementation(platform(libs.bom.configurate.kotlin))
}

tasks {
    copyJar {
        environment = "paper"
        jarFileName = "wakame-${project.version}.jar"
    }
}