@file:Suppress("UnstableApiUsage")

package cc.mewcraft.toolbox.command

import cc.mewcraft.toolbox.plugin
import com.google.inject.*
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.VelocityBrigadierMessage.*
import net.kyori.adventure.text.Component
import org.incendo.cloud.SenderMapper
import org.incendo.cloud.brigadier.suggestion.TooltipSuggestion
import org.incendo.cloud.execution.ExecutionCoordinator
import org.incendo.cloud.kotlin.extension.buildAndRegister
import org.incendo.cloud.minecraft.extras.suggestion.ComponentTooltipSuggestion
import org.incendo.cloud.velocity.CloudInjectionModule
import org.incendo.cloud.velocity.VelocityCommandManager
import org.koin.core.component.KoinComponent
import kotlin.time.measureTime

@Singleton
class ToolboxCommandManager(
    private val injector: Injector,
) : KoinComponent {

    companion object Shared {
        const val ROOT_COMMAND_NAME = "toolbox"
    }

    fun init() {
        val childInjector = injector.createChildInjector(
            CloudInjectionModule(
                CommandSource::class.java,
                ExecutionCoordinator.simpleCoordinator(),
                SenderMapper.identity()
            )
        )

        val commandManager = childInjector.getInstance(
            Key.get(object : TypeLiteral<VelocityCommandManager<CommandSource>>() {})
        )

        commandManager.buildAndRegister(ROOT_COMMAND_NAME) {
            permission(CommandPermissions.RELOAD)
            literal("reload")
            handler { context ->
                val elapsed = measureTime { plugin.reload() }
                context.sender().sendMessage(Component.text("Toolbox is reloaded in $elapsed"))
            }
        }

        commandManager.appendSuggestionMapper mapper@{ suggestion ->
            if (suggestion is ComponentTooltipSuggestion && suggestion.tooltip() != null) {
                return@mapper TooltipSuggestion.suggestion(
                    suggestion.suggestion(),
                    tooltip(suggestion.tooltip())
                )
            }
            return@mapper suggestion;
        }
    }
}