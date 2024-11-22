package cc.mewcraft.toolbox

import cc.mewcraft.toolbox.command.CommandManager
import cc.mewcraft.toolbox.config.Configs
import cc.mewcraft.toolbox.config.MAIN_CONFIG
import cc.mewcraft.toolbox.module.enderdragon.listener.EnderDragonListener
import cc.mewcraft.toolbox.module.hotfix.listener.HotfixListener
import cc.mewcraft.toolbox.module.networkfilter.NetworkFilterModule
import cc.mewcraft.toolbox.module.toolboxModules
import cc.mewcraft.toolbox.network.PacketEventInitializer
import cc.mewcraft.toolbox.network.networkModule
import cc.mewcraft.toolbox.plugin.ToolboxModule
import cc.mewcraft.toolbox.plugin.event.ToolboxReloadEvent
import cc.mewcraft.toolbox.plugin.pluginModule
import me.lucko.helper.plugin.KExtendedJavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.slf4j.Logger

val plugin: ToolboxPlugin
    get() = ToolboxPlugin.instance ?: error("plugin is not initialized yet!")

val logger: Logger
    get() = plugin.slF4JLogger

val isDebug: Boolean by MAIN_CONFIG.entry<Boolean>("debug")

class ToolboxPlugin : KExtendedJavaPlugin(), KoinComponent {
    companion object {
        internal var instance: ToolboxPlugin? = null
    }

    override suspend fun load() {
        PacketEventInitializer.load(this)

        startKoin {
            modules(
                rootModule(this@ToolboxPlugin),
                toolboxModules(),
                networkModule(),
                pluginModule(),
            )
        }

        // 加载每个 ToolboxModule, 记得按依赖顺序!
        try {
            loadToolboxModule<NetworkFilterModule>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun enable() {
        instance = this

        // 写入默认配置
        saveResource("config.yml")

        // 初始化指令
        CommandManager(this).initialize()

        // 初始化监听器
        registerTerminableListener(get<EnderDragonListener>())
        registerTerminableListener(get<HotfixListener>())

        // 初始化 PacketEvents
        PacketEventInitializer.enable()

        // 开启每个 ToolboxModule, 记得按依赖顺序!
        try {
            enableToolboxModule<NetworkFilterModule>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun disable() {
        instance = null

        // 关闭每个 ToolboxModule, 记得按依赖顺序!
        try {
            disableToolboxModule<NetworkFilterModule>()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        PacketEventInitializer.disable()

        stopKoin()
    }

    internal fun reload() {
        // 写入默认配置
        saveResource("config.yml")

        // 重载配置文件
        Configs.reload()

        // 触发重载事件
        ToolboxReloadEvent().callEvent()

        // 重载每个 ToolboxModule, 记得按依赖顺序!
        try {
            reloadToolboxModule<NetworkFilterModule>()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inline fun <reified T : ToolboxModule> handleToolboxModule(
        action: T.() -> Unit, actionName: String,
    ): T {
        val t = get<T>()
        with(t) {
            if (this.enabled) {
                action()
                logger.info("${T::class.simpleName} is $actionName!")
            }
        }
        return t
    }

    private inline fun <reified T : ToolboxModule> loadToolboxModule(): T {
        return handleToolboxModule<T>({ load() }, "loaded")
    }

    private inline fun <reified T : ToolboxModule> enableToolboxModule(): T {
        return handleToolboxModule<T>({ enable() }, "enabled")
    }

    private inline fun <reified T : ToolboxModule> disableToolboxModule(): T {
        return handleToolboxModule<T>({ disable() }, "disabled")
    }

    private inline fun <reified T : ToolboxModule> reloadToolboxModule(): T {
        return handleToolboxModule<T>({ reload() }, "reloaded")
    }
}