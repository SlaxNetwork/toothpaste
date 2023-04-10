package io.github.slaxnetwork

import com.github.shynixn.mccoroutine.minestom.launch
import io.github.slaxnetwork.commands.SwapKOTCInstanceCommand
import io.github.slaxnetwork.commands.TestInstanceCommand
import io.github.slaxnetwork.listener.registerGlobalEventNode
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.optifine.OptifineSupport
import net.minestom.server.extras.velocity.VelocityProxy

fun main() {
    initializeMiniMessage()

    val server = MinecraftServer.init()

    registerCommands()
    registerGlobalEventNode(server)

    createShutdownHook()

    server.launch {
        SessionDistributor.createServerPool()
    }

    OptifineSupport.enable()
    VelocityProxy.enable("fOJMwAzavxNh") // TODO: 4/9/2023 move to env.

    server.start("0.0.0.0", 25565)
}

/**
 * @since 0.0.1
 */
private fun registerCommands() {
    MinecraftServer.getCommandManager().run {
        register(TestInstanceCommand)
        register(SwapKOTCInstanceCommand)
    }
}

/**
 * @since 0.0.1
 */
private fun createShutdownHook() {
    MinecraftServer.getSchedulerManager()
        .buildShutdownTask {
            // remove from kyouko.
        }
}