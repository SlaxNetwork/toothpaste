package io.github.slaxnetwork.game.lobby

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.game.KOTCGameState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.instance.Instance

/**
 * @author Tech
 * @since 0.0.1
 */
class Lobby(
    val kotcGame: KOTCGameSession,
    val instance: Instance
) {
    private val lobbyNode = lobbyNode(this)

    val kotcGameState get() = kotcGame.state

    init {
        MinecraftServer.getGlobalEventHandler()
            .addChild(lobbyNode)
    }

    fun teleportToSpawn() {
        for(player in kotcGame.players) {
            player.minestomPlayer?.teleport(Pos(0.0, 100.0, 0.0))
        }
    }
}

private fun lobbyNode(lobby: Lobby): EventNode<PlayerEvent> {
    val node = EventNode.value(
        "kotc-lobby-listener",
        EventFilter.PLAYER
    ) {
        lobby.kotcGameState == KOTCGameState.IN_LOBBY
                && lobby.instance.uniqueId == it.instance?.uniqueId
    }

    node.addListener(PlayerChatEvent::class.java) { ev ->
        ev.recipients.removeIf {
            it.instance?.uniqueId != ev.player.instance?.uniqueId
        }

        ev.setChatFormat {
            return@setChatFormat Component.empty()
                .append(Component
                    .empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text(lobby.kotcGame.id))
                    .append(Component.text("]", NamedTextColor.GRAY))
                )
                .appendSpace()
                .append(Component.text(it.player.username, NamedTextColor.GOLD))
                .append(Component.text(":", NamedTextColor.GRAY))
                .appendSpace()
                .append(Component.text(it.message))
        }
    }

    return node
}