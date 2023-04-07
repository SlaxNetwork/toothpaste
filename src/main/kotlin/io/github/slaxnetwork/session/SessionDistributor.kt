package io.github.slaxnetwork.session

import io.github.slaxnetwork.game.GameInstance

/**
 * Create and manage pools of Game Instances.
 * @author Tech
 * @since 0.0.1
 */
object SessionDistributor {
    /**
     * Max game sessions that are allowed to be spawned
     * in one server instance.
     */
    private const val MAX_GAME_SESSIONS = 3

    private val GAME_POOL = mutableSetOf<GameInstance>()
    val gamePool: Set<GameInstance>
        get() = GAME_POOL

    /**
     * Create a pool of available game sessions that are a part
     * of this server.
     * @since 0.0.1
     */
    fun createServerPool() {
        GAME_POOL.clear()

        repeat(MAX_GAME_SESSIONS) {
            GAME_POOL.add(GameInstance())
        }

        initializeServerPool()
    }

    private fun initializeServerPool() {
        for(gameInstance in gamePool) {

        }
    }

    /**
     * Find the most desirable game instance.
     * @return Possible Game Instance.
     * @since 0.0.1
     */
    fun findGameInstance(): GameInstance? {
        return gamePool.firstOrNull()
    }

    /**
     * Find a game instance by its id.
     * @param id Game Instance id.
     * @return Possible Game Instance.
     * @since 0.0.1
     */
    fun findGameInstanceById(id: String): GameInstance? {
        return gamePool.firstOrNull()
    }
}