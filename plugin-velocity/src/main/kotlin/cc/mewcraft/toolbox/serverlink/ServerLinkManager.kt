package cc.mewcraft.toolbox.serverlink

import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.util.ServerLink
import org.spongepowered.configurate.CommentedConfigurationNode
import org.spongepowered.configurate.kotlin.extensions.getList
import org.spongepowered.configurate.reference.ConfigurationReference

class ServerLinkManager(
    private val config: ConfigurationReference<CommentedConfigurationNode>,
) {
    private val serverLinkList: MutableList<ServerLink> = mutableListOf()

    // 从配置文件中加载所有 ServerLink,
    // 并添加到 ServerLinkManager 中.
    // 将覆盖原有的所有数据.
    fun load() {
        val serverLinkList = config.node().node("server_links").getList<ServerLink>(emptyList())
        this.serverLinkList.clear()
        this.serverLinkList.addAll(serverLinkList)
    }

    fun setServerLink(player: Player) {
        player.setServerLinks(serverLinkList)
    }
}