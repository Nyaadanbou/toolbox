package cc.mewcraft.toolbox.hotfix.listener

import cc.mewcraft.toolbox.config.ConfigProvider
import cc.mewcraft.toolbox.config.MAIN_CONFIG
import cc.mewcraft.toolbox.logger
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object HotfixListener : Listener {

    val config: ConfigProvider = MAIN_CONFIG.node("hotfix")
    val resetInvulnerableOnJoin: Boolean by config.entry("reset_invulnerable_on_join")

    @EventHandler
    fun on(event: PlayerJoinEvent) {
        val player = event.player
        if (resetInvulnerableOnJoin) {
            player.isInvulnerable = false
            logger.info("[${player.name}] Reset invulnerable state (was: ${player.isInvulnerable})")
        }
    }
}