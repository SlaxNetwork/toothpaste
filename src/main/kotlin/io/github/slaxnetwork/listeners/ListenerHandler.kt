package io.github.slaxnetwork.listeners

import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.trait.PlayerEvent

fun registerBaseListeners() {
    val playerListenerNode = retrievePlayerListenerNode()

    MinecraftServer.getGlobalEventHandler()
        .addChild(playerListenerNode)
}

private fun retrievePlayerListenerNode(): EventNode<PlayerEvent> {
    val node = EventNode.type(
        "player-listener",
        EventFilter.PLAYER
    )

    node.addListener(PlayerLoginEvent::class.java) { ev ->

    }

    return node
}