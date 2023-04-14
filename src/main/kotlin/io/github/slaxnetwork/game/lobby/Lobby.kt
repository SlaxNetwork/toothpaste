package io.github.slaxnetwork.game.lobby

import io.github.slaxnetwork.game.KOTCGameSession
import io.github.slaxnetwork.game.KOTCGameSessionEventNode
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.instance.SharedInstance
import java.nio.file.Path

class Lobby(private val kotcGame: KOTCGameSession) {
    private val instanceManager: InstanceManager
        get() = MinecraftServer.getInstanceManager()

    var instance: SharedInstance? = null
        private set

    init {
        load()
    }

    fun load(): SharedInstance? {
        instance = instanceManager.createSharedInstance(lobbyInstance)

        instance?.eventNode()
            ?.addChild(KOTCGameSessionEventNode.createNode())
            ?.addChild(KOTCGameSessionEventNode.checkGameStartNode())
            ?.addChild(LobbyEventNode.createNode())

        return instance
    }

    fun unload() {
        instance?.let {
            instanceManager.unregisterInstance(it)
        }
    }

    companion object {
        /**
         * Shared Lobby instance providing for accessors.
         */
        val lobbyInstance: InstanceContainer

        init {
            val lobbyInstanceContainer = MinecraftServer.getInstanceManager()
                .createInstanceContainer()

            lobbyInstanceContainer.chunkLoader = AnvilLoader(Path.of("lobby").toAbsolutePath())

            lobbyInstance = lobbyInstanceContainer
        }
    }
}