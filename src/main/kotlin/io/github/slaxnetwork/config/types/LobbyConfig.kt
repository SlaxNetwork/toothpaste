package io.github.slaxnetwork.config.types

import io.github.slaxnetwork.config.serializer.PosSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minestom.server.coordinate.Pos

@Serializable
data class LobbyConfig(
    @Serializable(with = PosSerializer::class)
    @SerialName("spawn_point")
    val spawnPoint: Pos
)