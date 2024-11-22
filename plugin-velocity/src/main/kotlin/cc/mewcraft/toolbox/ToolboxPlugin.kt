package cc.mewcraft.toolbox

import cc.mewcraft.toolbox.command.ToolboxCommandManager
import cc.mewcraft.toolbox.config.*
import cc.mewcraft.toolbox.serverlink.ServerLinkListener
import cc.mewcraft.toolbox.serverlink.ServerLinkManager
import com.google.inject.Inject
import com.google.inject.Injector
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.reference.ConfigurationReference
import org.spongepowered.configurate.yaml.NodeStyle
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.outputStream

val plugin: ToolboxPlugin
    get() = ToolboxPlugin.instance ?: error("plugin is not initialized yet")

val logger: Logger
    get() = plugin.logger

@Plugin(
    id = "toolbox",
    name = "toolbox",
    version = "0.0.1-SNAPSHOT", // 记得同步更新
    dependencies = [
        Dependency(id = "kotlin"),
    ],
)
class ToolboxPlugin
@Inject constructor(
    val injector: Injector,
    val logger: Logger,
    val server: ProxyServer,
    @DataDirectory
    val dataDirectory: Path,
) {
    companion object {
        internal const val CONFIG_FILE_NAME = "config.yml"
        internal var instance: ToolboxPlugin? = null
    }

    lateinit var serverLinkManager: ServerLinkManager
        private set

    private val configurationReference = ConfigurationReference.fixed<CommentedConfigurationNode>(
        YamlConfigurationLoader.builder()
            .path(dataDirectory.resolve(CONFIG_FILE_NAME))
            .indent(2)
            .nodeStyle(NodeStyle.BLOCK)
            .defaultOptions { opts ->
                opts.implicitInitialization(false)
                opts.shouldCopyDefaults(false)
                opts.serializers { t ->
                    t.kregister(ServerLinkSerializer)
                    t.register(COMPONENT_SERIALIZER)
                }
            }
            .build()
    )

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent): Unit = runBlocking {
        instance = this@ToolboxPlugin

        // 初始化配置文件
        dataDirectory.createDirectories()
        saveResource(CONFIG_FILE_NAME)

        // 读取一下配置文件
        configurationReference.load()

        // 初始化 managers
        serverLinkManager = ServerLinkManager(configurationReference).apply(ServerLinkManager::load)

        // 注册 listeners
        server.eventManager.register(this@ToolboxPlugin, ServerLinkListener(serverLinkManager))

        // 注册 commands
        ToolboxCommandManager(injector).init()
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent): Unit = runBlocking {
        instance = null

        // 取消注册 listeners
        server.eventManager.unregisterListeners(this@ToolboxPlugin)
    }

    /**
     * 重新加载可以重载的数据.
     */
    fun reload() {
        // 重新加载配置文件
        configurationReference.load()
        logger.info("Reloaded configuration file.")

        // 重新从配置文件序列化
        serverLinkManager.load()
        logger.info("Reloaded server links.")
    }

    @Suppress("SameParameterValue")
    private fun saveResource(resourceName: String, overwrite: Boolean = false) {
        val outputPath = dataDirectory.resolve(resourceName)
        if (!overwrite && outputPath.toFile().exists()) {
            return
        }
        javaClass.getResourceAsStream("/$resourceName").use { inputStream ->
            inputStream?.copyTo(outputPath.outputStream())
        }
    }
}