package cc.mewcraft.toolbox.module.networkfilter.manager

import com.github.retrooper.packetevents.protocol.packettype.PacketType
import xyz.xenondevs.commons.provider.Provider
import java.util.EnumSet

class FilterManager(
    suppressedPacketTypes: Provider<EnumSet<PacketType.Play.Server>>,
) {
    private val suppressedPacketTypes: EnumSet<PacketType.Play.Server> by suppressedPacketTypes

    /* fun addPacketType(packetType: PacketType.Play.Server) {
        suppressedPacketTypes.add(packetType)
    }

    fun addPacketType(packetTypes: Collection<PacketType.Play.Server>) {
        suppressedPacketTypes.addAll(packetTypes)
    }

    fun addPacketType(vararg packetTypes: PacketType.Play.Server) {
        suppressedPacketTypes.addAll(packetTypes)
    }

    fun clearPacketType() {
        suppressedPacketTypes.clear()
    } */

    /**
     * 判断指定的数据包类型是否被抑制.
     */
    fun isSuppressed(packetType: PacketType.Play.Server): Boolean {
        return packetType in suppressedPacketTypes
    }
}