package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.player.GamePlayerSession
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.future.asDeferred
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventListener
import net.minestom.server.event.EventNode
import net.minestom.server.event.entity.EntityTickEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance
import java.util.UUID
import java.util.concurrent.CompletableFuture

object KOTCGameSessionEventNode {
    private const val NODE_ID = "kotc-game-event-node"
    private const val CHECK_GAME_START_NODE_ID = "kotc-game-start-event-node"

    /**
     * @since 0.0.1
     */
    fun createNode(): EventNode<KOTCGameSessionEvent> {
        val node = EventNode.type(
            NODE_ID,
            KOTCGameSessionEvent.filter()
        )

        return node.addChild(checkGameStartNode())
    }

    /**
     * @since 0.0.1
     */
    private fun checkGameStartNode(): EventNode<KOTCGameSessionEvent> {
        val node = EventNode.value(
            CHECK_GAME_START_NODE_ID,
            KOTCGameSessionEvent.filter()
        ) { !it.hasStarted }

        // check for start.
        node.addListener(KOTCSessionPlayerAddedEvent::class.java) { ev ->

        }

        // make sure min players are filled.
        node.addListener(KOTCSessionPlayerRemovedEvent::class.java) { ev ->

        }

        return node
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
interface KOTCGameSessionEvent : InstanceEvent {
    val kotcGame: KOTCGameSession

    companion object {
        /**
         * @since 0.0.1
         */
        fun filter(): EventFilter<KOTCGameSessionEvent, KOTCGameSession> {
            return EventFilter.from(
                KOTCGameSessionEvent::class.java,
                KOTCGameSession::class.java,
                KOTCGameSessionEvent::kotcGame
            )
        }
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
data class KOTCSessionPlayerAddedEvent(
    val playerSession: GamePlayerSession,
    override val kotcGame: KOTCGameSession
) : KOTCGameSessionEvent  {
    override fun getInstance(): Instance {
        return kotcGame.currentInstance
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
data class KOTCSessionPlayerReconnectEvent(
    val playerSession: GamePlayerSession,
    override val kotcGame: KOTCGameSession
) : KOTCGameSessionEvent {
    override fun getInstance(): Instance {
        return kotcGame.currentInstance
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
data class KOTCSessionPlayerRemovedEvent(
    val uuid: UUID,
    override val kotcGame: KOTCGameSession
) : KOTCGameSessionEvent {
    override fun getInstance(): Instance {
        return kotcGame.currentInstance
    }
}