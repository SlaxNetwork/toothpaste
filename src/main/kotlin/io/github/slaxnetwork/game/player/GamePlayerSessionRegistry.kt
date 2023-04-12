package io.github.slaxnetwork.game.player

import io.github.slaxnetwork.game.KOTCGameSession
import java.util.UUID

/**
 * @author Tech
 * @since 0.0.1
 */
object GamePlayerSessionRegistry {
    private val PLAYERS = mutableSetOf<GamePlayerSession>()
    val players: Set<GamePlayerSession>
        get() = PLAYERS

    fun add(uuid: UUID, kotcGame: KOTCGameSession): GamePlayerSession {
        if(findByUUID(uuid) != null) {
            throw IllegalArgumentException("player already exists.")
        }

        val session = GamePlayerSession(
            uuid,
            kotcGame
        )

        PLAYERS.add(session)

        return session
    }

    fun remove(uuid: UUID) {
        PLAYERS.removeIf { it.uuid == uuid }
    }

    fun findByUUID(uuid: UUID): GamePlayerSession? {
        return players.firstOrNull { it.uuid == uuid }
    }
}