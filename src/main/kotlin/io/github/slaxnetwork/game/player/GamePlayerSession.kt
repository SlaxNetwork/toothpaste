package io.github.slaxnetwork.game.player

import io.github.slaxnetwork.game.KOTCGameSession
import net.minestom.server.entity.Player
import java.util.UUID

/**
 * @author Tech
 * @since 0.0.1
 */
class GamePlayerSession(
    val uuid: UUID,
    val kotcGame: KOTCGameSession
) {
    var connected: Boolean = true

    val statistics = GamePlayerSessionStatistics()

    val minestomPlayer: Player?
        get() = Player.getEntity(uuid) as? Player
}