@file:Suppress("UnstableApiUsage")

package cc.mewcraft.toolbox.command

import cc.mewcraft.toolbox.command.command.ReloadCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.plugin.Plugin
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.paper.PaperCommandManager

class CommandManager(
    plugin: Plugin,
) {
    private val commandManager: PaperCommandManager<CommandSourceStack> = PaperCommandManager
        .builder()
        .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
        .buildOnEnable(plugin)


    fun initialize() {

        // Register commands
        with(commandManager) {
            command(ReloadCommand)
        }
    }
}