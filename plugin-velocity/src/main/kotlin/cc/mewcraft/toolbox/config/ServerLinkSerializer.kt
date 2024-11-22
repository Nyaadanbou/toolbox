package cc.mewcraft.toolbox.config

import com.velocitypowered.api.util.ServerLink
import net.kyori.adventure.text.Component
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import java.lang.reflect.Type

object ServerLinkSerializer : TypeSerializer<ServerLink> {
    override fun deserialize(type: Type, node: ConfigurationNode): ServerLink? {
        val labelOrType = run {
            val builtInType = runCatching { node.node("label").get<ServerLink.Type>() }.getOrNull()
            if (builtInType != null) {
                return@run builtInType
            }
            val customLabel = node.node("label").get<Component>()
            customLabel
        }
        val uri = node.node("uri").get<String>()

        return when (labelOrType) {
            is ServerLink.Type -> ServerLink.serverLink(labelOrType, uri)
            is Component -> ServerLink.serverLink(labelOrType, uri)
            else -> throw IllegalArgumentException("neither valid label nor valid type: $labelOrType")
        }
    }

    override fun serialize(type: Type, obj: ServerLink?, node: ConfigurationNode) {
        throw UnsupportedOperationException()
    }
}