package cc.mewcraft.toolbox.config

import cc.mewcraft.toolbox.util.javaTypeOf
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.get
import org.spongepowered.configurate.serialize.TypeSerializer
import org.spongepowered.configurate.serialize.TypeSerializerCollection
import kotlin.reflect.*

// TypeSerializerCollection.Builder extensions

inline fun <reified T> TypeSerializerCollection.Builder.kregister(serializer: TypeSerializer<T>): TypeSerializerCollection.Builder {
    return this.register({ javaTypeOf<T>() == it }, serializer)
}


// ConfigurationNode extensions


inline fun <reified T> ConfigurationNode.krequire(): T {
    return this.krequire(typeOf<T>())
}

fun <T : Any> ConfigurationNode.krequire(clazz: KClass<T>): T {
    val ret = this.get(clazz) ?: throw NoSuchElementException(
        "Can't get the value of type '${clazz}' at '${path().joinToString(".")}'"
    )
    return ret
}

fun <T> ConfigurationNode.krequire(type: KType): T {
    val ret = this.get(type) ?: throw NoSuchElementException(
        "Can't get the value of type '${type}' at '${path().joinToString(".")}'"
    )
    @Suppress("UNCHECKED_CAST")
    return ret as T
}
