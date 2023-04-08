package io.github.slaxnetwork.party

import io.github.slaxnetwork.listener.nodes.player.party.partyListenerNode
import net.minestom.server.MinecraftServer
import net.minestom.server.event.EventNode

/**
 * @author Tech
 * @since 0.0.1
 */
object PartyHandler {
    private val partyNode = partyNode()

    private val PARTIES = mutableSetOf<String>()
    val parties: Set<String>
        get() = PARTIES

    init {
        MinecraftServer.getGlobalEventHandler()
            .addChild(partyNode)
    }


}

private fun partyNode(): EventNode<*> {
    val node = EventNode.all("party-listener")

    return node
}