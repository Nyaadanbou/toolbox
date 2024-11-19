package cc.mewcraft.toolbox.config

import cc.mewcraft.toolbox.plugin
import cc.mewcraft.toolbox.util.reloadable
import org.bukkit.configuration.Configuration

object Config {
    private val config: Configuration by reloadable { plugin.config }

    val enderDragonDroppedExp: Int by reloadable { config.getInt("ender_dragon.dropped_exp", 0) }
}