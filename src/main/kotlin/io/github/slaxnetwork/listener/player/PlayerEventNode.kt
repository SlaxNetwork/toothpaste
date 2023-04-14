package io.github.slaxnetwork.listener.player

import io.github.slaxnetwork.game.KOTCSessionPlayerAddedEvent
import io.github.slaxnetwork.game.KOTCSessionPlayerReconnectEvent
import io.github.slaxnetwork.game.KOTCSessionPlayerDisconnectedEvent
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.GlobalEventHandler
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.trait.PlayerEvent

/**
 * @author Tech
 * @since 0.0.1
 */
object PlayerEventNode {
    private val globalEventHandler: GlobalEventHandler
        get() = MinecraftServer.getGlobalEventHandler()

    private const val NODE_ID = "player-event-node"
    private const val KOTC_SESSION_COORDINATOR_NODE_ID = "player-kotc-session-coordinator-node"
    private const val KOTC_SESSION_CONNECTION_NODE_ID = "player-kotc-session-connection-node"

    /**
     * @since 0.0.1
     */
    fun createNode(server: MinecraftServer): EventNode<PlayerEvent> {
        val node = EventNode.type(
           NODE_ID,
           EventFilter.PLAYER
        )



        return node.addChild(kotcSessionCoordinatorNode(server))
            .addChild(kotcSessionConnectionNode(server))
//        node.addListener(PlayerLoginEvent::class.java) { ev ->
//            val playerSession = GamePlayerSessionRegistry.findByUUID(ev.player.uuid)
//                ?: return@addListener
//
//            globalEventHandler.call(KOTCSessionPlayerReconnectEvent(
//                playerSession
//            ))
//        }
//
//        node.addListener(PlayerDisconnectEvent::class.java) { ev ->
//            val playerSession = GamePlayerSessionRegistry.findByUUID(ev.player.uuid)
//                ?: return@addListener
//
//            globalEventHandler.call(KOTCSessionPlayerRemovedEvent(
//                playerSession.uuid,
//                playerSession.kotcGame
//            ))
//        }
//
//        return node.addChild(kotcSessionHandlerNode(server))
    }

    /**
     * @since 0.0.1
     */
    private fun kotcSessionCoordinatorNode(server: MinecraftServer): EventNode<PlayerEvent> {
        val node = EventNode.value(
            KOTC_SESSION_COORDINATOR_NODE_ID,
            EventFilter.PLAYER
        ) { GamePlayerSessionRegistry.findByUUID(it.uuid) == null }

        node.addListener(PlayerLoginEvent::class.java) { ev ->
            val player = ev.player

            val party = DummyAPI.findParty(player.uuid)
            val kotcGame = if (party == null) {
                SessionDistributor.findGameSession()
            } else {
                DummyPartyHandler.findPartyGameSession(party.leader)
                    ?: SessionDistributor.findGameSession()
            }

            if (kotcGame == null) {
                player.kick("No session found.")
                return@addListener
            }

            val playerSession = GamePlayerSessionRegistry.add(
                player.uuid,
                kotcGame
            )
            ev.setSpawningInstance(kotcGame.currentInstance)

            kotcGame.instanceEventNode.call(KOTCSessionPlayerAddedEvent(playerSession, kotcGame))
        }

        return node
    }

    private fun kotcSessionConnectionNode(server: MinecraftServer): EventNode<PlayerEvent> {
        val node = EventNode.type(
            KOTC_SESSION_CONNECTION_NODE_ID,
            EventFilter.PLAYER
        )

        node.addListener(PlayerLoginEvent::class.java) { ev ->
            val playerSession = GamePlayerSessionRegistry.findByUUID(ev.player.uuid)
                ?: return@addListener
            val eventNode = playerSession.kotcGame.instanceEventNode

            eventNode.call(KOTCSessionPlayerReconnectEvent(playerSession, playerSession.kotcGame))
        }

        node.addListener(PlayerDisconnectEvent::class.java) { ev ->
            val playerSession = GamePlayerSessionRegistry.findByUUID(ev.player.uuid)
                ?: return@addListener
            val eventNode = playerSession.kotcGame.instanceEventNode

            eventNode.call(KOTCSessionPlayerDisconnectedEvent(playerSession.uuid, playerSession.kotcGame))
        }

        return node
    }
}