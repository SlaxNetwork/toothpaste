package io.github.slaxnetwork.listener.nodes.player

import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.listener.nodes.player.party.partyListenerNode
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.trait.PlayerEvent
import java.util.UUID

fun playerListenerNode(server: MinecraftServer): EventNode<PlayerEvent> {
    val node = EventNode.type(
        "player-listener",
        EventFilter.PLAYER
    )

    val partyNode = partyListenerNode()

    node.addSuspendingListener(server, PlayerLoginEvent::class.java) { ev ->
        val player = ev.player

        val party = DummyAPI.findParty(player.uuid)
        val session = if(party == null) {
            SessionDistributor.findGameSession()
        } else {
            DummyPartyHandler.findPartyGameSession(party.leader)
        }

        if(session == null) {
            player.kick("No session found.")
            return@addSuspendingListener
        }

        ev.setSpawningInstance(session.instance)
        player.respawnPoint = Pos(90.0, 100.0, 0.0)
        player.gameMode = GameMode.CREATIVE
    }

    return node.addChild(partyNode)
}

object DummyAPI {
    fun findParty(uuid: UUID): DummyPartyModel? {
        return null
    }
}

object DummyPartyHandler {
    fun findPartyGameSession(uuid: UUID): KOTCGameSession? {
        val serv = SessionDistributor.gamePool
            .firstOrNull { it.players.map { c -> c.uuid }.contains(uuid) }

        return serv
    }
}

data class DummyPartyModel(
    val leader: UUID,
    val members: Set<UUID>
)