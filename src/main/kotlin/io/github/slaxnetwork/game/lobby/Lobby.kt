package io.github.slaxnetwork.game.lobby

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.game.KOTCGameState
import io.github.slaxnetwork.game.lobby.node.checkLobbyStartVoteNode
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import net.minestom.server.instance.SharedInstance
import java.nio.file.Path

/**
 * @author Tech
 * @since 0.0.1
 */
class Lobby(
    val kotcGame: KOTCGameSession,
    val instance: Instance
) {
    private val lobbyNode = lobbyNode(this)

    private val voteHandler = LobbyVoteHandler()

    val kotcGameState get() = kotcGame.state

    init {
        val node = EventNode.value(
            "kotc-lobby-listener",
            EventFilter.INSTANCE
        ) { kotcGameState == KOTCGameState.IN_LOBBY }

        node.addChild(lobbyNode)

        instance.eventNode()
            .addChild(node)
    }

    fun teleportToSpawn() {
        for(player in kotcGame.players) {
            player.minestomPlayer?.teleport(Pos(0.0, 100.0, 0.0))
        }
    }

    companion object {
        val lobbyInstance: Instance

        init {
            val instance = MinecraftServer.getInstanceManager()
                .createInstanceContainer()

            MinecraftServer.getInstanceManager().createSharedInstance(instance)

            instance.chunkLoader = AnvilLoader(Path.of("lobby").toAbsolutePath())

            lobbyInstance = instance
        }
    }
}

/**
 * @since 0.0.1
 */
private fun lobbyNode(lobby: Lobby): EventNode<InstanceEvent> {
    val node = EventNode.value(
        "kotc-lobby-listener",
        EventFilter.INSTANCE
    ) {
        lobby.kotcGameState == KOTCGameState.IN_LOBBY
    }

    node.addListener(PlayerDisconnectEvent::class.java) { ev ->
        val player = ev.entity as? Player
            ?: return@addListener

        // Remove them from registry.
        lobby.kotcGame.removePlayer(player.uuid)
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
        .addChild(checkLobbyStartVoteNode(lobby))
}