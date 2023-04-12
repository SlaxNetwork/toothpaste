package io.github.slaxnetwork.commands

import io.github.slaxnetwork.game.KOTCGameState
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.minestom.server.command.builder.Command
import net.minestom.server.entity.Player

object TestInstanceCommand : Command("confidence") {
    init {
        setDefaultExecutor { sender, context ->
            val player = sender as? Player
                ?: return@setDefaultExecutor

            val playerSession = GamePlayerSessionRegistry.findByUUID(player.uuid)
                ?: return@setDefaultExecutor

            val kotcGame = playerSession.kotcGame
            if(kotcGame.state == KOTCGameState.IN_LOBBY) {
                kotcGame.state = KOTCGameState.ENDING
            } else {
                kotcGame.state = KOTCGameState.IN_LOBBY
            }

            sender.sendMessage("State = ${kotcGame.state}")

        }
    }
}