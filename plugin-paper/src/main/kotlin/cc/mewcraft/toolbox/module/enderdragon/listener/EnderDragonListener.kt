package cc.mewcraft.toolbox.module.enderdragon.listener

import cc.mewcraft.toolbox.config.ConfigProvider
import cc.mewcraft.toolbox.config.MAIN_CONFIG
import cc.mewcraft.toolbox.logger
import org.bukkit.entity.EnderDragon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

object EnderDragonListener : Listener {

    val config: ConfigProvider = MAIN_CONFIG.node("ender_dragon")
    val enderDragonDroppedExp: Int by config.entry("dropped_exp")

    // 末影龙死掉时修改掉落经验为固定值
    @EventHandler(ignoreCancelled = true)
    private fun on(e: EntityDeathEvent) {
        val entity = e.entity
        if (entity !is EnderDragon)
            return

        e.droppedExp = enderDragonDroppedExp
        logger.info("EnderDragon droppedExp set to $enderDragonDroppedExp")
    }
}