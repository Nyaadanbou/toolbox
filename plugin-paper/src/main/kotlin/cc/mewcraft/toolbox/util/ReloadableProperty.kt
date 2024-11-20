package cc.mewcraft.toolbox.util

import cc.mewcraft.toolbox.plugin.event.ToolboxReloadEvent
import cc.mewcraft.toolbox.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ReloadableProperty<T>(
    private val loader: () -> T,
) : ReadOnlyProperty<Any?, T> {
    private var value: T? = null

    init {
       plugin.server.pluginManager.registerEvents(ReloadableListener(), plugin)
    }

    fun get(): T {
        val value = this.value
        if (value == null) {
            val createdValue = loader()
            this.value = createdValue
            return createdValue
        }
        return value
    }

    private fun reload() {
        value = null
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }

    private inner class ReloadableListener : Listener {
        @EventHandler
        fun onReload(event: ToolboxReloadEvent) {
            reload()
        }
    }
}