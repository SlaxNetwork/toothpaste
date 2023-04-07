package io.github.slaxnetwork.game.player

import io.github.slaxnetwork.game.KOTCGame
import net.minestom.server.entity.Player
import java.util.UUID

object GamePlayerSessionRegistry {
    private val PLAYERS = mutableSetOf<GamePlayerSession>()
    val players: Set<GamePlayerSession>
        get() = PLAYERS

    fun addPlayer(player: Player, game: KOTCGame): GamePlayerSession {
        val session = GamePlayerSession(
            player.uuid,
            game
        )

        PLAYERS.add(session)

        return session
    }

    fun findPlayer(uuid: UUID): GamePlayerSession? {
        return players.firstOrNull { it.uuid == uuid }
    }
}