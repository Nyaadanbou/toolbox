plugins {
    id("nyaadanbou-conventions.repositories")
    id("toolbox-conventions.commons")
}

group = "cc.mewcraft.toolbox"
version = "0.0.1-SNAPSHOT"

dependencies {
    api(local.commons.collections)
    api(local.commons.provider)
    api(platform(libs.bom.configurate.yaml))
    api(platform(libs.bom.configurate.kotlin))
    api(platform(local.koin.bom))
    api(local.koin.core) {
        exclude("org.jetbrains.kotlin")
    }
    api(local.koin.core.coroutines) {
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
    }

    testImplementation(local.koin.test) { exclude("org.jetbrains.kotlin") }
    testImplementation(local.koin.test.junit5) { exclude("org.jetbrains.kotlin") }
}

tasks {
    shadowJar {
        relocate("org.spongepowered.configurate", "cc.mewcraft.wakame.external.config")
    }
}
