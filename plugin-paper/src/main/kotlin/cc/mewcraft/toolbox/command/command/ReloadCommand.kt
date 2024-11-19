@file:Suppress("UnstableApiUsage")

package cc.mewcraft.toolbox.command.command

import cc.mewcraft.toolbox.command.CommandConstants
import cc.mewcraft.toolbox.command.CommandPermissions
import cc.mewcraft.toolbox.command.buildAndAdd
import cc.mewcraft.toolbox.command.suspendingHandler
import cc.mewcraft.toolbox.plugin
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.Command
import org.incendo.cloud.CommandFactory
import org.incendo.cloud.CommandManager
import org.incendo.cloud.description.Description
import org.incendo.cloud.kotlin.extension.commandBuilder
import kotlin.system.measureTimeMillis

object ReloadCommand : CommandFactory<CommandSourceStack> {
    const val RELOAD_LITERAL = "reload"

    override fun createCommands(commandManager: CommandManager<CommandSourceStack>): List<Command<out CommandSourceStack>> {
        return buildList {
            commandManager.commandBuilder(
                name = CommandConstants.ROOT_COMMAND,
                description = Description.of("Commands for Toolbox")
            ) {
                permission(CommandPermissions.RELOAD)
                literal(RELOAD_LITERAL)
                suspendingHandler(context = plugin.minecraftDispatcher) { context ->
                    val sender = context.sender().sender
                    val reloadTime = measureTimeMillis {
                        plugin.reload()
                    }

                    sender.sendPlainMessage("Toolbox reloaded in $reloadTime ms")
                }
            }.buildAndAdd(this)
        }
    }
}