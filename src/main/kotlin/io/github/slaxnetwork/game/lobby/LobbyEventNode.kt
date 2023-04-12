package io.github.slaxnetwork.game.lobby

import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

/**
 * @author Tech
 * @since 0.0.1
 */
object LobbyEventNode {
    private const val NODE_ID = "lobby-event-node"

    /**
     * @since 0.0.1
     */
    fun createNode(): EventNode<LobbyEvent> {
        val node = EventNode.type(
            NODE_ID,
            LobbyEvent.filter()
        )

        return node
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
interface LobbyEvent : InstanceEvent {
    val lobby: Lobby

    companion object {
        /**
         * @since 0.0.1
         */
        fun filter(): EventFilter<LobbyEvent, Lobby> {
            return EventFilter.from(
                LobbyEvent::class.java,
                Lobby::class.java,
                LobbyEvent::lobby
            )
        }
    }
}