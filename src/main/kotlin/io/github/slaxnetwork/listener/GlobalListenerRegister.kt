package io.github.slaxnetwork.listener

import io.github.slaxnetwork.listener.nodes.player.playerListenerNode
import net.minestom.server.MinecraftServer

/**
 * @since 0.0.1
 */
fun registerGlobalListeners(server: MinecraftServer) {
    MinecraftServer.getGlobalEventHandler()
        .addChild(playerListenerNode(server))
}