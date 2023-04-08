package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.lobby.Lobby
import io.github.slaxnetwork.game.player.GamePlayerSession
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.trait.EntityEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import java.io.File
import java.nio.file.Path

/**
 * @author Tech
 * @since 0.0.1
 */
class KOTCGameSession(
    val id: Int,
    val instance: Instance
) {
    val lobby = Lobby(this, instance)

    private val _players = mutableSetOf<GamePlayerSession>()
    val players: Set<GamePlayerSession>
        get() = _players

    var hasStarted: Boolean = false
        private set

    var state: KOTCGameState = KOTCGameState.IN_LOBBY

    init {
        instance.eventNode()
            .addChild(kotcGameSessionNode(this))
    }

    fun addPlayer(player: Player): GamePlayerSession {
        return GamePlayerSessionRegistry.addPlayer(player, this)
    }

    companion object {
        fun createInstance(): Instance {
            val instance = MinecraftServer.getInstanceManager()
                .createInstanceContainer()

            instance.chunkLoader = AnvilLoader(Path.of("test").toAbsolutePath())

            return instance
        }
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
private fun kotcGameSessionNode(kotcGame: KOTCGameSession): EventNode<InstanceEvent> {
    val node = EventNode.type(
        "kotc-game-session-listener",
        EventFilter.INSTANCE
    )

    node.addListener(AddEntityToInstanceEvent::class.java) { ev ->
        val player = ev.entity as? Player
            ?: return@addListener

        // in session
        if(kotcGame.hasStarted) {
            player.respawnPoint = Pos.ZERO
        // in lobby
        } else {
            player.respawnPoint = Pos(0.0, 103.0, 0.0)
        }

        player.gameMode = GameMode.CREATIVE
    }

    node.addListener(RemoveEntityFromInstanceEvent::class.java) { ev ->
        val player = ev.entity as? Player
            ?: return@addListener
    }

    node.addListener(PlayerDisconnectEvent::class.java) { ev ->
        val player = ev.player

        if(kotcGame.hasStarted) {
            GamePlayerSessionRegistry.findPlayer(player.uuid)
                ?.connected = false
        } else {
            GamePlayerSessionRegistry.removePlayer(player.uuid)
        }
    }

    return node
}