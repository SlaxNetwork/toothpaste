package io.github.slaxnetwork.listener.player

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.session.SessionDistributor
import java.util.*

object DummyAPI {
    fun findParty(uuid: UUID): DummyPartyModel? {
        return null
    }
}

object DummyPartyHandler {
    fun findPartyGameSession(uuid: UUID): KOTCGameSession? {
//        val serv = SessionDistributor.gamePool
//            .firstOrNull { it.players.map { c -> c.uuid }.contains(uuid) }
//
//        return serv
        return null
    }
}

data class DummyPartyModel(
    val leader: UUID,
    val members: Set<UUID>
)