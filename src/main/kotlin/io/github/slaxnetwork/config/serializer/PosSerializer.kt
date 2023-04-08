package io.github.slaxnetwork.config.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minestom.server.coordinate.Pos

/**
 * @author Tech
 * @since 0.0.1
 */
object PosSerializer : KSerializer<Pos> {

    /**
     * Surrogate to help serialize a Minestom [Pos].
     * @author Tech
     * @since 0.0.1
     */
    @Serializable
    private data class PosSurrogate(
        val x: Double,
        val y: Double,
        val z: Double,
        val yaw: Float? = null,
        val pitch: Float? = null
    ) {
        fun toPos(): Pos {
            return if(yaw == null || pitch == null) {
                Pos(x, y, z)
            } else {
                Pos(x, y, z, yaw, pitch)
            }
        }
    }

    private val surrogateSerializer = PosSurrogate.serializer()

    override val descriptor = surrogateSerializer.descriptor

    override fun deserialize(decoder: Decoder): Pos {
        return surrogateSerializer.deserialize(decoder)
            .toPos()
    }

    override fun serialize(encoder: Encoder, value: Pos) {
        surrogateSerializer.serialize(encoder, PosSurrogate(value.x, value.y, value.z, value.yaw, value.pitch))
    }
}