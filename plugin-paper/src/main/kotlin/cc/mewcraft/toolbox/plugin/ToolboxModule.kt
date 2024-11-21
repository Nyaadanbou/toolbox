package cc.mewcraft.toolbox.plugin

import cc.mewcraft.toolbox.config.ConfigProvider
import cc.mewcraft.toolbox.config.MAIN_CONFIG
import cc.mewcraft.toolbox.logger
import cc.mewcraft.toolbox.plugin
import com.github.retrooper.packetevents.event.*
import me.lucko.helper.terminable.Terminable
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.composite.CompositeTerminable
import org.bukkit.event.Listener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class ToolboxModule(
    moduleName: String,
) : Terminable, TerminableConsumer, KoinComponent {

    private val moduleTerminables: CompositeTerminable = CompositeTerminable.create()

    private val networkEventManager: EventManager by inject()

    val config: ConfigProvider = MAIN_CONFIG.node(moduleName)
    val enabled: Boolean by config.entry("enabled")

    abstract fun load()

    abstract fun enable()

    abstract fun disable()

    abstract fun reload()

    protected fun registerBukkitListener(listener: Listener, requiredPlugin: String? = null) {
        if (requiredPlugin != null) {
            if (plugin.isPluginPresent(requiredPlugin)) {
                plugin.registerTerminableListener(listener)
            } else {
                logger.warn("Plugin $requiredPlugin is not present, skipping listener registration")
            }
        } else {
            plugin.registerTerminableListener(listener)
        }
    }

    @Suppress("SameParameterValue")
    protected fun registerNetworkListener(listener: PacketListener, priority: PacketListenerPriority) {
        networkEventManager.registerListener(listener, priority)
    }

    override fun <T : AutoCloseable?> bind(terminable: T & Any): T & Any {
        return moduleTerminables.bind(terminable)
    }

    override fun close() {
        moduleTerminables.closeAndReportException()
    }
}