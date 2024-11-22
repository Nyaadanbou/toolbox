package cc.mewcraft.toolbox.config

import cc.mewcraft.toolbox.util.javaTypeOf
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.spongepowered.configurate.serialize.ScalarSerializer
import org.spongepowered.configurate.serialize.TypeSerializer
import java.util.function.Predicate

val COMPONENT_SERIALIZER: ScalarSerializer<Component> = TypeSerializer.of(
    javaTypeOf<Component>(),
    { v: Component, pass: Predicate<Class<*>> ->
        MiniMessage.miniMessage().serialize(v)
    },
    { v: Any ->
        MiniMessage.miniMessage().deserialize(v.toString())
    }
)