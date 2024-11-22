package cc.mewcraft.toolbox.module.networkfilter.manager

import org.bukkit.entity.Player
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import com.github.retrooper.packetevents.protocol.player.User as PacketUser

class UserManager : Iterable<User> {
    private val cache: ConcurrentHashMap<UUID, User> = ConcurrentHashMap()

    fun loadUser(playerUuid: UUID): User {
        return cache.computeIfAbsent(playerUuid, ::User)
    }

    fun loadUser(player: Player): User {
        return loadUser(player.uniqueId)
    }

    fun loadUser(user: PacketUser): User {
        return loadUser(user.uuid)
    }

    fun unloadUser(playerUuid: UUID) {
        cache.remove(playerUuid)
    }

    fun unloadUser(user: PacketUser) {
        unloadUser(user.uuid)
    }

    fun unloadUser(player: Player) {
        unloadUser(player.uniqueId)
    }

    fun cleanup() {
        cache.clear()
    }

    override fun iterator(): Iterator<User> {
        return cache.values.iterator()
    }
}