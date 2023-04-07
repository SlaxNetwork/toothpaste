package io.github.slaxnetwork.listener

import io.github.slaxnetwork.listener.nodes.lobby.lobbyListenerNode
import io.github.slaxnetwork.listener.nodes.player.playerListenerNode
import net.minestom.server.MinecraftServer

fun registerGlobalListeners(server: MinecraftServer) {
    MinecraftServer.getGlobalEventHandler()
        .addChild(lobbyListenerNode())
        .addChild(playerListenerNode(server))
}