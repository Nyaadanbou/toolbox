package cc.mewcraft.toolbox.module.networkfilter.listener

import cc.mewcraft.toolbox.module.networkfilter.manager.UserManager
import net.ess3.api.events.AfkStatusChangeEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EssentialsListener(
    private val userManager: UserManager,
) : Listener {
    @EventHandler
    fun on(event: AfkStatusChangeEvent) {
        val isAfk = event.value
        val essUser = event.controller
        val player = essUser.base
        val user = userManager.loadUser(player)

        // 玩家进入挂机状态, 开启过滤器
        // 玩家退出挂机状态, 关闭过滤器
        user.isFilterEnabled = isAfk
    }
}