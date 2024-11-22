package cc.mewcraft.toolbox.serverlink

import com.velocitypowered.api.event.*
import com.velocitypowered.api.event.player.configuration.PlayerConfigurationEvent

class ServerLinkListener(
    private val serverLinkManager: ServerLinkManager,
) {
    // 当玩家进入配置阶段后, 设置 ServerLink
    @Subscribe(order = PostOrder.NORMAL)
    fun on(event: PlayerConfigurationEvent, continuation: Continuation) {
        val player = event.player
        try {
            serverLinkManager.setServerLink(player)
            continuation.resume()
        } catch (e: Exception) {
            continuation.resumeWithException(e)
        }
    }
}