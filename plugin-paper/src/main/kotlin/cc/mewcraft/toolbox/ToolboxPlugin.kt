package cc.mewcraft.toolbox

import cc.mewcraft.toolbox.command.CommandManager
import cc.mewcraft.toolbox.config.Configs
import cc.mewcraft.toolbox.module.enderdragon.enderDragonModule
import cc.mewcraft.toolbox.module.enderdragon.listener.EnderDragonListener
import cc.mewcraft.toolbox.module.hotfix.hotfixModule
import cc.mewcraft.toolbox.module.hotfix.listener.HotfixListener
import cc.mewcraft.toolbox.module.networkfilter.networkFilterModule
import cc.mewcraft.toolbox.network.PacketEventInitializer
import cc.mewcraft.toolbox.plugin.event.ToolboxReloadEvent
import cc.mewcraft.toolbox.plugin.pluginModule
import me.lucko.helper.plugin.KExtendedJavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.slf4j.Logger

val plugin: ToolboxPlugin
    get() = ToolboxPlugin.instance ?: error("Plugin is not initialized yet.")

val logger: Logger
    get() = plugin.slF4JLogger

class ToolboxPlugin : KExtendedJavaPlugin(), KoinComponent {
    companion object {
        internal var instance: ToolboxPlugin? = null
    }

    override suspend fun load() {
        startKoin {
            modules(
                rootModule(this@ToolboxPlugin),
                enderDragonModule(),
                hotfixModule(),
                networkFilterModule(),
                pluginModule(),
            )
        }

        PacketEventInitializer.load(this)
    }

    override suspend fun enable() {
        instance = this

        // 写入默认配置
        saveResource("config.yml")

        // 初始化监听器
        registerTerminableListener(get<EnderDragonListener>())
        registerTerminableListener(get<HotfixListener>())

        // 初始化指令
        CommandManager(this).initialize()

        // 初始化 PacketEvents
        PacketEventInitializer.enable()
    }

    override suspend fun disable() {
        instance = null
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
    }
}