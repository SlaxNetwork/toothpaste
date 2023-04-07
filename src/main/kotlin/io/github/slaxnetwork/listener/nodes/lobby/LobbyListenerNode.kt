package io.github.slaxnetwork.listener.nodes.lobby

import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.trait.PlayerEvent

fun lobbyListenerNode(): EventNode<*> {
    val node = EventNode.all("kotc-lobby")

    return node.addChild(lobbyChatNode())
}

private fun lobbyChatNode(): EventNode<PlayerEvent> {
    val lobbyChatNode = EventNode.value(
        "kotc-lobby-chat",
        EventFilter.PLAYER
    ) { player ->
        val gamePlayer = GamePlayerSessionRegistry.findPlayer(player.uuid)
            ?: return@value true

        !(gamePlayer.kotcGame.hasStarted)
    }

    lobbyChatNode.addListener(PlayerChatEvent::class.java) { ev ->
        ev.setChatFormat {
            return@setChatFormat Component.empty()
                .append(Component.text(it.player.username, NamedTextColor.WHITE))
                .append(Component.text(": "))
                .append(Component.text(it.message, NamedTextColor.GOLD))
        }
    }

    return lobbyChatNode
}