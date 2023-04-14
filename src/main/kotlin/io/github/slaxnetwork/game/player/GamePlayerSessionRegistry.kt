package io.github.slaxnetwork.game.player

import io.github.slaxnetwork.game.KOTCGameSession
import java.util.UUID

/**
 * @author Tech
 * @since 0.0.1
 */
object GamePlayerSessionRegistry {
    private val PLAYERS = mutableMapOf<UUID, GamePlayerSession>()
    val players: Map<UUID, GamePlayerSession>
        get() = PLAYERS

    fun add(uuid: UUID, kotcGame: KOTCGameSession): GamePlayerSession {
        if(findByUUID(uuid) != null) {
            throw IllegalArgumentException("player already exists.")
        }

        val session = GamePlayerSession(
            uuid,
            kotcGame
        )

        PLAYERS[uuid] = session

        return session
    }

    fun remove(uuid: UUID) {
        PLAYERS.remove(uuid)
    }

    fun findByUUID(uuid: UUID): GamePlayerSession? {
        return players[uuid]
    }
}