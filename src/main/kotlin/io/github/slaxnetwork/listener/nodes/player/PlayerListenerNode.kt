package io.github.slaxnetwork.listener.nodes.player

import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.trait.PlayerEvent
import net.minestom.server.instance.block.Block

fun playerListenerNode(server: MinecraftServer): EventNode<PlayerEvent> {
    val node = EventNode.type(
        "player-listener",
        EventFilter.PLAYER
    )

    // TODO: 4/7/2023 move instance to KOTC Game
    val con = MinecraftServer.getInstanceManager()
        .createInstanceContainer()
    con.setGenerator { unit ->
        unit.modifier()
            .fillHeight(0, 1, Block.GRASS_BLOCK)
    }

    node.addListener(PlayerLoginEvent::class.java) { ev ->
        ev.player.respawnPoint = Pos(0.0, 2.0, 0.0)
        ev.setSpawningInstance(con)
    }

    node.addSuspendingListener(server, PlayerLoginEvent::class.java) { ev ->
        val session = SessionDistributor.findGameInstance()
        if(session == null) {
            ev.player.kick("invalid session")
            return@addSuspendingListener
        }

        session.addPlayer(ev.player)
    }

    return node
}