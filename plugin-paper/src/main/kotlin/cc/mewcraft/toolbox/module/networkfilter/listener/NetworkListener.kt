package cc.mewcraft.toolbox.module.networkfilter.listener

import cc.mewcraft.toolbox.module.networkfilter.manager.FilterManager
import cc.mewcraft.toolbox.module.networkfilter.manager.UserManager
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType

class NetworkListener(
    private val userManager: UserManager,
    private val filterManager: FilterManager,
) : PacketListener {
    override fun onPacketSend(event: PacketSendEvent) {
        val eventUser = event.user
        val packetType = event.packetType
        if (packetType is PacketType.Play.Server && filterManager.isSuppressed(packetType)) {
            // 过滤器抑制了该数据包

            val networkUser = userManager.loadUser(eventUser)
            if (networkUser.isFilterEnabled) {
                // 玩家开启了过滤器

                event.isCancelled = true
            }
        }
    }
}