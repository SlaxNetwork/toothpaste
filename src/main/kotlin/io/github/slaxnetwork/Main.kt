package io.github.slaxnetwork

import io.github.slaxnetwork.listeners.registerBaseListeners
import io.github.slaxnetwork.session.SessionDistributor
import net.minestom.server.MinecraftServer
import net.minestom.server.extras.velocity.VelocityProxy

fun main() {
    val server = MinecraftServer.init()

    registerBaseListeners()

    SessionDistributor.createServerPool()

    VelocityProxy.enable("fOJMwAzavxNh")
    server.start("0.0.0.0", 25565)
}