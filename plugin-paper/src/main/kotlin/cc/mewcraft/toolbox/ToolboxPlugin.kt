package cc.mewcraft.toolbox

import cc.mewcraft.toolbox.command.CommandManager
import cc.mewcraft.toolbox.enderdragon.EnderDragonDeathListener
import cc.mewcraft.toolbox.event.ToolboxReloadEvent
import me.lucko.helper.plugin.KExtendedJavaPlugin
import org.bukkit.event.HandlerList

class ToolboxPlugin : KExtendedJavaPlugin() {
    companion object {
        internal var instance: ToolboxPlugin? = null
    }

    override suspend fun enable() {
        instance = this
        CommandManager(this).init()
        reload()
        registerSuspendListener(EnderDragonDeathListener)
    }

    override suspend fun disable() {
        instance = null
        HandlerList.unregisterAll(EnderDragonDeathListener)
    }

    internal fun reload() {
        saveDefaultConfig()
        reloadConfig()

        ToolboxReloadEvent().callEvent()
    }
}

internal val plugin: ToolboxPlugin
    get() = ToolboxPlugin.instance ?: error("Plugin is not initialized yet.")