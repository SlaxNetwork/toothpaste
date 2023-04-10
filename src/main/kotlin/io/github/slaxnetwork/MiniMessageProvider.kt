package io.github.slaxnetwork

import net.kyori.adventure.text.minimessage.MiniMessage

internal lateinit var mm: MiniMessage
    private set

fun initializeMiniMessage() {
    mm = MiniMessage.builder()
        .build()
}