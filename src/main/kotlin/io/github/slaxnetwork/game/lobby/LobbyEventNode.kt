package io.github.slaxnetwork.game.lobby

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.game.KOTCGameState
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.player.PlayerChatEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance

fun getLobbyEventNode(lobby: Lobby): EventNode<InstanceEvent> {
    val node = EventNode.value(
        "kotc-session-lobby-event-node",
        EventFilter.INSTANCE
    ) { lobby.kotcGameState == KOTCGameState.IN_LOBBY }

    node.addListener(AddEntityToInstanceEvent::class.java) { ev ->
        val player = ev.entity as? Player
            ?: return@addListener

        val pos = Pos(0.0, 103.0, 0.0)

        player.respawnPoint = pos
        player.scheduleNextTick {
            player.teleport(pos)
        }

        node.call(CheckStartVoteEvent(lobby.kotcGame))
    }

    node.addListener(PlayerChatEvent::class.java) { ev ->
        ev.recipients.removeIf {
            it.instance?.uniqueId != ev.player.instance?.uniqueId
        }

        ev.setChatFormat {
            return@setChatFormat Component.empty()
                .append(
                    Component
                    .empty()
                    .append(Component.text("[", NamedTextColor.GRAY))
                    .append(Component.text(lobby.kotcGame.id))
                    .append(Component.text("]", NamedTextColor.GRAY))
                )
                .appendSpace()
                .append(Component.text(it.player.username, NamedTextColor.GOLD))
                .append(Component.text(":", NamedTextColor.GRAY))
                .appendSpace()
                .append(Component.text(it.message))
        }
    }

    return node
}

/**
 * @author Tech
 * @since 0.0.1
 */
data class CheckStartVoteEvent(val kotcGame: KOTCGameSession) : InstanceEvent {
    override fun getInstance(): Instance {
        return kotcGame.instance
    }
}

/**
 * @since 0.0.1
 */
fun getLobbyVoteEventNode(): EventNode<InstanceEvent> {
    val node = EventNode.type(
        "kotc-session-lobby-vote-event-node",
        EventFilter.INSTANCE
    )

    node.addListener(CheckStartVoteEvent::class.java) { ev ->
        ev.kotcGame
        println("CALLED CHECK START VOTE!!")
    }

    return node
}

//fun getLobbyVoteEventNode(lobby: Lobby): EventNode<InstanceEvent> {
//    val node = EventNode.value(
//        ""
//    )
//}