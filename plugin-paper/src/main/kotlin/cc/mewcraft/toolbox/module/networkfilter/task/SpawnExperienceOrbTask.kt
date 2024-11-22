package cc.mewcraft.toolbox.module.networkfilter.task

import cc.mewcraft.toolbox.module.networkfilter.manager.UserManager
import org.bukkit.Bukkit
import org.bukkit.entity.ExperienceOrb
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.scheduler.BukkitRunnable

class SpawnExperienceOrbTask(
    private val userManager: UserManager,
    private val experienceAmount: Int,
) : BukkitRunnable() {
    override fun run() {
        for (user in userManager) {
            val playerUuid = user.uuid

            if (user.isFilterEnabled) {
                // 玩家开启了过滤器, 生成经验球

                val player = Bukkit.getServer().getEntity(playerUuid)
                if (player == null) {
                    continue
                }

                player.world.spawn(
                    player.location,
                    ExperienceOrb::class.java,
                    CreatureSpawnEvent.SpawnReason.CUSTOM,
                ) { orb ->
                    orb.experience = experienceAmount
                }
            }
        }
    }
}