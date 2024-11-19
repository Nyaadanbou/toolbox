package cc.mewcraft.toolbox.enderdragon

import cc.mewcraft.toolbox.config.Config
import org.bukkit.entity.EnderDragon
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

object EnderDragonDeathListener : Listener {
    @EventHandler
    private fun on(e: EntityDeathEvent) {
        val entity = e.entity
        if (entity !is EnderDragon)
            return

        e.droppedExp = Config.enderDragonDroppedExp
    }
}