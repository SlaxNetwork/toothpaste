package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.player.GamePlayerSession
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance
import java.util.UUID

/**
 * @author Tech
 * @since 0.0.1
 */
interface KOTCGameSessionEvent : InstanceEvent {
    val kotcGame: KOTCGameSession

    companion object {
        fun getEventFilter(): EventFilter<KOTCGameSessionEvent, KOTCGameSession> {
            return EventFilter.from(KOTCGameSessionEvent::class.java, KOTCGameSession::class.java, KOTCGameSessionEvent::kotcGame)
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
) : KOTCGameSessionEvent {
    override fun getInstance(): Instance {
        return kotcGame.instance
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
        return kotcGame.instance
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
object KOTCGameSessionEventNode {
    private const val NODE_ID = "kotc-game-session-event-node"
    private const val CHECK_START_KOTC_NODE_ID = "kotc-game-session-start-event-node"

    /**
     * @since 0.0.1
     */
    fun createNode(): EventNode<KOTCGameSessionEvent> {
        val node = EventNode.type(
            NODE_ID,
            KOTCGameSessionEvent.getEventFilter()
        )

        node.addListener(KOTCSessionPlayerRemovedEvent::class.java) { ev ->
            val kotcGame = ev.kotcGame

            if(kotcGame.hasStarted) {
                GamePlayerSessionRegistry.findPlayer(ev.uuid)?.let { playerSession ->
                    playerSession.connected = false
                }
            } else {
                GamePlayerSessionRegistry.removePlayer(ev.uuid)
            }
        }

        return node
    }

    /**
     * @since 0.0.1
     */
    private fun checkStartKOTCSessionNode(): EventNode<KOTCGameSessionEvent> {
        val node = EventNode.value(
            CHECK_START_KOTC_NODE_ID,
            KOTCGameSessionEvent.getEventFilter()
        ) { kotcGame ->
            kotcGame.state == KOTCGameState.IN_LOBBY && !kotcGame.hasStarted
        }

        node.addListener(KOTCSessionPlayerAddedEvent::class.java) { ev ->
            val playerAmount = ev.kotcGame.players.size
            val startHandler = ev.kotcGame.startHandler

            if(playerAmount >= KOTCGameSession.MIN_PLAYER_START && !startHandler.isCountdownActive) {
                println("slow start, players = $playerAmount")
                startHandler.startGameCountdown(fast = false)
            } else if(playerAmount >= KOTCGameSession.MAX_PLAYER_START && startHandler.isCountdownActive) {
                println("fast start, players = $playerAmount")
                startHandler.startGameCountdown(fast = true)
            }
        }

        node.addListener(KOTCSessionPlayerRemovedEvent::class.java) { ev ->
            val playerAmount = ev.kotcGame.players.size
            val startHandler = ev.kotcGame.startHandler

            if(playerAmount < KOTCGameSession.MIN_PLAYER_START) {
                startHandler.stopGameCountdown()
                println("disposed countdown.")
            }
        }

        return node
    }
}