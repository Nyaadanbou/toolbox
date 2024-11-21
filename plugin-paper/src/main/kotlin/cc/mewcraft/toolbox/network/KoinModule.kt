package cc.mewcraft.toolbox.network

import com.github.retrooper.packetevents.PacketEvents
import com.github.retrooper.packetevents.PacketEventsAPI
import com.github.retrooper.packetevents.event.EventManager
import org.koin.core.module.Module
import org.koin.dsl.module

fun networkModule(): Module = module {
    single<PacketEventsAPI<*>> { PacketEvents.getAPI() }
    single<EventManager> { get<PacketEventsAPI<*>>().eventManager }
}