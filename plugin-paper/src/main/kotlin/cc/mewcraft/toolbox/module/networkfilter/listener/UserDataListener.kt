package cc.mewcraft.toolbox.module.networkfilter.listener

import cc.mewcraft.toolbox.module.networkfilter.manager.UserManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class UserDataListener(
    private val userManager: UserManager,
) : Listener {

    @EventHandler
    fun on(event: PlayerQuitEvent) {
        val player = event.player

        // 玩家退出游戏时卸载用户数据
        userManager.unloadUser(player)
    }
}