package io.github.slaxnetwork

import io.github.slaxnetwork.listener.registerGlobalListeners
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.velocity.VelocityProxy

fun main() {
    val server = MinecraftServer.init()

    registerGlobalListeners(server)

    SessionDistributor.createServerPool()

    VelocityProxy.enable("fOJMwAzavxNh")
    server.start("0.0.0.0", 25565)
}