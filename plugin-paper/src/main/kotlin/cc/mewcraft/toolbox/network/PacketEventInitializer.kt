@file:Suppress("UnstableApiUsage")

package cc.mewcraft.toolbox.network

import com.github.retrooper.packetevents.PacketEvents
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder
import org.bukkit.plugin.Plugin

object PacketEventInitializer {
    fun load(plugin: Plugin) {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(plugin))
        PacketEvents.getAPI().load()
    }

    fun enable() {
        PacketEvents.getAPI().apply {
            settings.checkForUpdates(false)
            settings.reEncodeByDefault(false)
        }.init()
    }

    fun disable() {
        PacketEvents.getAPI().terminate()
    }
}