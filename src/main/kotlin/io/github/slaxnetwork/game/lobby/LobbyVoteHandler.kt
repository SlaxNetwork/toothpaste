package io.github.slaxnetwork.game.lobby

import io.github.slaxnetwork.game.KOTCGameSession
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent

/**
 * @author Tech
 * @since 0.0.1
 */
class LobbyVoteHandler {
    var currentVoteHandler: VoteHandler? = null
        private set

    fun startGameVote() {
        currentVoteHandler = GameVote
    }

    sealed interface VoteHandler {
        fun start()

        fun end()
    }

    object GameVote : VoteHandler {
        private val previouslyPlayed = mutableSetOf<String>()

        val gameVotePool = mutableListOf<String>()

        override fun start() {
        }

        override fun end() {
        }
    }
}

private fun lobbyVoteHandlerNode(kotcGame: KOTCGameSession): EventNode<InstanceEvent> {
    val node = EventNode.type(
        "lobby-vote-listener",
        EventFilter.INSTANCE
    )

    return node
}