package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.lobby.Lobby
import io.github.slaxnetwork.game.player.GamePlayerSession
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.Player
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
        private set

    init {

    }

    fun addPlayer(player: Player) {
        GamePlayerSessionRegistry.addPlayer(player, this)
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