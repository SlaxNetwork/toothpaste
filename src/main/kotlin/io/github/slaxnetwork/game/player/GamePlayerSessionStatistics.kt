package io.github.slaxnetwork.game.player

/**
 * @author Tech
 * @since 0.0.1
 */
class GamePlayerSessionStatistics {
    var rubiesEarned: Int = 0
        private set

    fun addRubiesEarned(amount: Int) {
        rubiesEarned += amount
    }
}