package cc.mewcraft.toolbox.config

import cc.mewcraft.toolbox.RootKoinModule
import cc.mewcraft.toolbox.ToolboxPlugin
import cc.mewcraft.toolbox.config.Configs.getKoin
import org.jetbrains.annotations.TestOnly
import org.koin.core.component.KoinComponent
import org.koin.core.qualifier.named
import org.spongepowered.configurate.loader.AbstractConfigurationLoader
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File
import kotlin.io.path.Path

val MAIN_CONFIG: ConfigProvider by lazy {
    Configs.YAML["config.yml"]
}

/**
 * The object that manages the configuration providers.
 */
object Configs : KoinComponent {
    val YAML: YamlConfigs = YamlConfigs()

    fun reload() {
        YAML.reload()
    }

    @TestOnly
    fun cleanup() {
        YAML.cleanup()
    }
}

sealed class BasicConfigs<T : AbstractConfigurationLoader<*>, B : AbstractConfigurationLoader.Builder<B, T>> {
    /**
     * The map of config providers.
     *
     * K - the name of the config provider e.g. "config.yml", "kizami.yml"
     * V - the config provider
     */
    private val configProviders: MutableMap<String, ConfigProvider> = HashMap()

    /**
     * Builds a [config provider][ConfigProvider] with the specified options.
     */
    fun build(relPath: String, builder: B.() -> Unit): ConfigProvider {
        val path = Path(relPath)
        return configProviders.getOrPut(path.toString()) { createConfigProvider(relPath, builder) }
    }

    /**
     * Gets a [config provider][ConfigProvider] with the default options.
     */
    operator fun get(relPath: String): ConfigProvider {
        val path = Path(relPath)
        return build(path.toString()) {}
    }

    /**
     * Reloads all the config providers.
     */
    fun reload() {
        for (entry in configProviders) {
            entry.value.update()
        }
    }

    /**
     * Cleans up the config providers.
     */
    @TestOnly
    fun cleanup() {
        configProviders.clear()
    }

    /**
     * Gets the config file.
     */
    protected fun getConfigPath(path: String): File {
        return getKoin().getOrNull<ToolboxPlugin>()?.getBundledFile(path) // we're in a server environment
            ?: getKoin().get<File>(named(RootKoinModule.DATA_FOLDER)).resolve(path) // we're in a test environment
    }

    /**
     * Creates a config provider.
     */
    protected abstract fun createConfigProvider(
        relPath: String, builder: B.() -> Unit,
    ): ConfigProvider
}

class YamlConfigs : BasicConfigs<YamlConfigurationLoader, YamlConfigurationLoader.Builder>() {
    override fun createConfigProvider(
        relPath: String,
        builder: YamlConfigurationLoader.Builder.() -> Unit,
    ): ConfigProvider {
        val file = getConfigPath(relPath)
        val path = file.toPath()
        return YamlFileConfigProvider(path, relPath, builder)
    }
}
