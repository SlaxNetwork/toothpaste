package io.github.slaxnetwork.listener.player

import com.github.shynixn.mccoroutine.minestom.addSuspendingListener
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.trait.PlayerEvent

/**
 * @author Tech
 * @since 0.0.1
 */
class PlayerEventNode(private val server: MinecraftServer) {
    companion object {
        private const val NODE_ID = "player-event-node"
        private const val KOTC_SESSION_HANDLER_NODE = "player-kotc-session-handler-node"
    }

    /**
     * @since 0.0.1
     */
    fun createNode(): EventNode<PlayerEvent> {
           val node = EventNode.type(
               NODE_ID,
               EventFilter.PLAYER
           )

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
            kotcSession.addPlayer(player)
            // set their spawning instance.
            ev.setSpawningInstance(kotcSession.instance)
        }

        return node
    }
}