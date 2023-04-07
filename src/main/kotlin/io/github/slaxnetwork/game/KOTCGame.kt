package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.player.GamePlayerSession
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.minestom.server.entity.Player

class KOTCGame(
    val id: Int
) {
    private val _players = mutableSetOf<GamePlayerSession>()
    val players: Set<GamePlayerSession>
        get() = _players

    var hasStarted: Boolean = false
        private set

    fun addPlayer(player: Player) {
        GamePlayerSessionRegistry.addPlayer(player, this)
    }
}