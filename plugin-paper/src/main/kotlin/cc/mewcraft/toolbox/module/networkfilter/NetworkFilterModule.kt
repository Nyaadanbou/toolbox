package cc.mewcraft.toolbox.module.networkfilter

import cc.mewcraft.toolbox.module.networkfilter.listener.*
import cc.mewcraft.toolbox.module.networkfilter.manager.FilterManager
import cc.mewcraft.toolbox.module.networkfilter.manager.UserManager
import cc.mewcraft.toolbox.module.networkfilter.task.SpawnExperienceOrbTask
import cc.mewcraft.toolbox.plugin.ToolboxModule
import com.github.retrooper.packetevents.event.PacketListenerPriority
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import me.lucko.helper.Schedulers
import me.lucko.helper.scheduler.Task
import org.bukkit.entity.EntityType
import xyz.xenondevs.commons.provider.immutable.map
import java.util.EnumSet

class NetworkFilterModule() : ToolboxModule("network_filter") {

    val idlingExperienceAmount = config.entry<Int>("idling_experience", "amount")
    val idlingExperienceInterval = config.entry<Long>("idling_experience", "interval")
    val ignoredEntityTypes = config.entry<List<EntityType>>("ignored_entity_types").map { EnumSet.copyOf(it) }
    val suppressedPacketTypes = config.entry<List<PacketType.Play.Server>>("suppressed_packet_types").map { EnumSet.copyOf(it) }

    lateinit var userManager: UserManager
        private set
    lateinit var filterManager: FilterManager
        private set

    // 创造经验球实体的定时任务
    // null = 未启用
    private var spawnExperienceOrbTask: Task? = null

    override fun load() {
        // no ops
    }

    override fun enable() {
        userManager = UserManager()
        filterManager = FilterManager(suppressedPacketTypes)

        // 注册 Bukkit Listener
        registerBukkitListener(UserDataListener(userManager))
        registerBukkitListener(EssentialsListener(userManager), "Essentials")

        // 注册 Network Listener
        registerNetworkListener(NetworkListener(userManager, filterManager), PacketListenerPriority.NORMAL)

        // 注册定时任务
        spawnExperienceOrbTask = startTask()
    }

    override fun disable() {
        getKoin().getOrNull<UserManager>()?.cleanup()
        spawnExperienceOrbTask?.close()
    }

    override fun reload() {
        // 配置文件已重载好
        // ...

        // 重新启动定时任务
        spawnExperienceOrbTask?.close()
        spawnExperienceOrbTask = startTask()
    }

    private fun startTask(): Task? {
        val idlingExperienceAmount: Int by idlingExperienceAmount
        val idlingExperienceInterval: Long by idlingExperienceInterval

        if (idlingExperienceAmount <= 0 || idlingExperienceInterval <= 0) {
            return null
        }

        // 注册定时任务
        val repeatingTask = Schedulers.sync().runRepeating(
            /* runnable = */SpawnExperienceOrbTask(userManager, idlingExperienceAmount),
            /* delayTicks = */ 100L,
            /* intervalTicks = */ idlingExperienceInterval,
        ).apply(::bind)

        return repeatingTask
    }
}