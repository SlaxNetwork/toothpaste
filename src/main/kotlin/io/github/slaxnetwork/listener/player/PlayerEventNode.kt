package io.github.slaxnetwork.listener.player

import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import io.github.slaxnetwork.game.KOTCSessionPlayerAddedEvent
import io.github.slaxnetwork.game.KOTCSessionPlayerRemovedEvent
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
    private const val KOTC_SESSION_HANDLER_NODE = "player-kotc-session-handler-node"

    /**
     * @since 0.0.1
     */
    fun createNode(server: MinecraftServer): EventNode<PlayerEvent> {
       val node = EventNode.type(
           NODE_ID,
           EventFilter.PLAYER
       )

        node.addListener(PlayerDisconnectEvent::class.java) { ev ->
            val playerSession = GamePlayerSessionRegistry.findPlayer(ev.player.uuid)
                ?: return@addListener

            globalEventHandler.call(KOTCSessionPlayerRemovedEvent(
                playerSession.uuid,
                playerSession.kotcGame
            ))
        }

        return node.addChild(kotcSessionHandlerNode(server))
    }

    /**
     * @since 0.0.1
     */
    private fun kotcSessionHandlerNode(server: MinecraftServer): EventNode<PlayerEvent> {
        val node = EventNode.type(
            KOTC_SESSION_HANDLER_NODE,
            EventFilter.PLAYER
        )

        node.addSuspendingListener(server, PlayerLoginEvent::class.java) { ev ->
            val player = ev.player

            val party = DummyAPI.findParty(player.uuid)
            val kotcSession = if(party == null) {
                SessionDistributor.findGameSession()
            } else {
                DummyPartyHandler.findPartyGameSession(party.leader)
                    ?: SessionDistributor.findGameSession()
            }

            if(kotcSession == null) {
                player.kick("No session found.")
                return@addSuspendingListener
            }

            // add them to the session.
            val playerSession = kotcSession.addPlayer(player)
            // set their spawning instance.
            ev.setSpawningInstance(kotcSession.instance)

            globalEventHandler.call(KOTCSessionPlayerAddedEvent(playerSession))
        }

        return node
    }
}