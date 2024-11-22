package cc.mewcraft.toolbox.module.networkfilter.manager

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

class User(
    val uuid: UUID,
) {
    /**
     * 该用户的协程作用域.
     */
    val scope = CoroutineScope(CoroutineName("network-user-${uuid}"))

    /**
     * 获取用户的玩家实例.
     */
    val player: Player
        get() = Bukkit.getPlayer(uuid) ?: error("the player reference is null")

    /**
     * 该用户的发包过滤是否启用?
     */
    var isFilterEnabled: Boolean = false

    /**
     * 清理与该用户相关的资源.
     */
    fun cleanup() {
        scope.cancel("user cleanup")
    }
}