package cc.mewcraft.toolbox.plugin

import cc.mewcraft.toolbox.config.ConfigProvider
import cc.mewcraft.toolbox.config.MAIN_CONFIG
import me.lucko.helper.terminable.TerminableConsumer
import me.lucko.helper.terminable.composite.CompositeTerminable

abstract class ToolboxModule(
    val moduleName: String,
) : TerminableConsumer {

    private val terminables: CompositeTerminable = CompositeTerminable.create()

    val config: ConfigProvider = MAIN_CONFIG.node(moduleName)
    val isEnabled: Boolean by config.entry("enabled")

    abstract fun load()

    abstract fun enable()

    abstract fun disable()

    abstract fun requiredPlugins(): List<String>

    override fun <T : AutoCloseable?> bind(terminable: T & Any): T & Any {
        return terminables.bind(terminable)
    }
}