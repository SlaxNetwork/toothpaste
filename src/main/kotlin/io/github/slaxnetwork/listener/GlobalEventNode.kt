package io.github.slaxnetwork.listener

import io.github.slaxnetwork.listener.player.PlayerEventNode
import net.minestom.server.MinecraftServer

/**
 * @since 0.0.1
 */
fun registerGlobalEventNode(server: MinecraftServer) {
    MinecraftServer.getGlobalEventHandler()
        .addChild(PlayerEventNode(server).createNode())
}