package io.github.slaxnetwork.game.lobby

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.game.KOTCGameState
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import java.nio.file.Path

/**
 * @author Tech
 * @since 0.0.1
 */
class Lobby(
    val kotcGame: KOTCGameSession,
    val instance: Instance
) {
    private val voteHandler = LobbyVoteHandler()

    val kotcGameState get() = kotcGame.state

    init {
        val node = EventNode.value(
            "kotc-lobby-listener",
            EventFilter.INSTANCE
        ) { kotcGameState == KOTCGameState.IN_LOBBY }

        instance.eventNode()
            .addChild(
                node.addChild(getLobbyEventNode(this))
                    .addChild(getLobbyVoteEventNode())
            )
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