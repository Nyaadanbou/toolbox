plugins {
    id("nyaadanbou-conventions.repositories")
    id("nyaadanbou-conventions.copy-jar")
    id("toolbox-conventions.commons")
}

group = "cc.mewcraft.toolbox"
version = "0.0.1-SNAPSHOT"

dependencies {
    implementation(project(":common"))
    implementation(platform(libs.bom.configurate.yaml))
    implementation(platform(libs.bom.configurate.kotlin))
    implementation(platform(libs.bom.cloud.velocity))
    implementation(platform(libs.bom.cloud.kotlin)) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    compileOnly(local.velocity); kapt(local.velocity)
}

tasks {
    shadowJar {
        relocate("org.spongepowered.configurate", "cc.mewcraft.wakame.toolbox.config")
    }
    copyJar {
        environment = "velocity"
        jarFileName = "toolbox-${project.version}.jar"
    }
}
