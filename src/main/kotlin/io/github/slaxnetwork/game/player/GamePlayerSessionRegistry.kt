package io.github.slaxnetwork.game.player

import io.github.slaxnetwork.game.KOTCGameSession
import net.minestom.server.entity.Player
import java.util.UUID

/**
 * @author Tech
 * @since 0.0.1
 */
object GamePlayerSessionRegistry {
    private val PLAYERS = mutableSetOf<GamePlayerSession>()
    val players: Set<GamePlayerSession>
        get() = PLAYERS

    fun addPlayer(player: Player, game: KOTCGameSession): GamePlayerSession {
        if(findPlayer(player.uuid) != null) {
            throw IllegalArgumentException("player already exists.")
        }

        val session = GamePlayerSession(
            player.uuid,
            game
        )

        PLAYERS.add(session)

        return session
    }

    fun removePlayer(uuid: UUID) {
        PLAYERS.removeIf { it.uuid == uuid }
    }

    fun findPlayer(uuid: UUID): GamePlayerSession? {
        return players.firstOrNull { it.uuid == uuid }
    }
}