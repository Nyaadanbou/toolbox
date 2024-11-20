package cc.mewcraft.toolbox.plugin.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ToolboxReloadEvent : Event() {
    override fun getHandlers(): HandlerList = HANDLERS

    companion object {
        private val HANDLERS = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList = HANDLERS
    }
}