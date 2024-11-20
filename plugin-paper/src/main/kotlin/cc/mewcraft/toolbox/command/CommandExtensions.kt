@file:Suppress("UnstableApiUsage")

package cc.mewcraft.toolbox.command

import cc.mewcraft.toolbox.plugin
import com.github.shynixn.mccoroutine.bukkit.minecraftDispatcher
import com.github.shynixn.mccoroutine.bukkit.scope
import io.papermc.paper.command.brigadier.CommandSourceStack
import kotlinx.coroutines.CoroutineScope
import org.incendo.cloud.Command
import org.incendo.cloud.kotlin.MutableCommandBuilder
import org.incendo.cloud.kotlin.coroutines.SuspendingExecutionHandler
import org.incendo.cloud.kotlin.coroutines.extension.suspendingHandler
import kotlin.coroutines.CoroutineContext

/**
 * Specify a suspending command execution handler.
 */
fun <C : Any> MutableCommandBuilder<C>.suspendingHandler(
    scope: CoroutineScope = plugin.scope,
    context: CoroutineContext = plugin.minecraftDispatcher,
    handler: SuspendingExecutionHandler<C>,
): MutableCommandBuilder<C> = mutate {
    it.suspendingHandler(scope, context, handler)
}

/**
 * Builds this command and adds it to the given [commands].
 *
 * @param C the sender type
 * @param commands the command list
 */
fun <C : CommandSourceStack> MutableCommandBuilder<C>.buildAndAdd(commands: MutableList<Command<out C>>) {
    commands += this.build()
}