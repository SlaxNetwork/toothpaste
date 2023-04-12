package io.github.slaxnetwork.game.microgame

/**
 * @author Tech
 * @since 0.0.1
 */
abstract class MicroGame(
    val map: MicroGameMap
) {
    var state: MicroGameState = MicroGameState.IN_PRE_GAME
        private set
}