package io.github.slaxnetwork.game.lobby.node

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.game.KOTCGameState
import io.github.slaxnetwork.game.lobby.Lobby
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance

/**
 * @author Tech
 * @since 0.0.1
 */
data class CheckLobbyStartVoteEvent(val lobby: Lobby) : InstanceEvent {
    override fun getInstance(): Instance {
        return lobby.instance
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
fun checkLobbyStartVoteNode(lobby: Lobby): EventNode<InstanceEvent> {
    val node = EventNode.value(
        "kotc-lobby-check-start-listener",
        EventFilter.INSTANCE
    ) {
        lobby.kotcGameState == KOTCGameState.IN_LOBBY && !lobby.kotcGame.hasStarted
    }

    node.addListener(AddEntityToInstanceEvent::class.java) { ev ->
        node.call(CheckLobbyStartVoteEvent(lobby))
    }

    node.addListener(RemoveEntityFromInstanceEvent::class.java) { ev ->
        node.call(CheckLobbyStartVoteEvent(lobby))
    }

    node.addListener(CheckLobbyStartVoteEvent::class.java) { ev ->
        val kotcGame = lobby.kotcGame
        val startHandler = kotcGame.startHandler

        val playerAmount = kotcGame.players.size

        if(playerAmount < KOTCGameSession.MIN_PLAYER_START) {
            if(startHandler.isCountdownActive) {
                startHandler.stopGameCountdown()

                return@addListener
            }
        }

        // slow countdown.
        if(playerAmount > KOTCGameSession.MIN_PLAYER_START && !startHandler.isCountdownActive) {
            startHandler.startGameCountdown(fast = false)
        // fast countdown.
        } else if(playerAmount == KOTCGameSession.MAX_PLAYER_START&& startHandler.isCountdownActive) {
            startHandler.startGameCountdown(fast = true)
        }
    }

    return node
}