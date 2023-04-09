package io.github.slaxnetwork

import com.github.shynixn.mccoroutine.minestom.launch
import io.github.slaxnetwork.commands.TestInstanceCommand
import io.github.slaxnetwork.listener.registerGlobalListeners
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.optifine.OptifineSupport
import net.minestom.server.extras.velocity.VelocityProxy

fun main() {
    val server = MinecraftServer.init()

    val commandManager = MinecraftServer.getCommandManager()
    commandManager.register(TestInstanceCommand)

    registerGlobalListeners(server)

    server.launch {
        SessionDistributor.createServerPool()
    }

    OptifineSupport.enable()
    VelocityProxy.enable("fOJMwAzavxNh")
    server.start("0.0.0.0", 25565)
}