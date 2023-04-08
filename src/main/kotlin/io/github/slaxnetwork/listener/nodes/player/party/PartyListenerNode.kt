package io.github.slaxnetwork.listener.nodes.player.party

import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.PlayerEvent

fun partyListenerNode(): EventNode<PlayerEvent> {
    val node = EventNode.type(
        "party-listener",
        EventFilter.PLAYER
    )

    return node
}