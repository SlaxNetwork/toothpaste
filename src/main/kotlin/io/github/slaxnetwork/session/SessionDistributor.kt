package io.github.slaxnetwork.session

import io.github.slaxnetwork.game.KOTCGame

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

    private val GAME_POOL = mutableSetOf<KOTCGame>()
    val gamePool: Set<KOTCGame>
        get() = GAME_POOL

    /**
     * Create a pool of available game sessions that are a part
     * of this server.
     * @since 0.0.1
     */
    fun createServerPool() {
        GAME_POOL.clear()

        repeat(MAX_GAME_SESSIONS) {
            GAME_POOL.add(KOTCGame(
                id = it
            ))
        }

        registerServerPool()
    }

    /**
     * Register the server pool onto Kyouko.
     * @since 0.0.1
     */
    private fun registerServerPool() {
        for(gameInstance in gamePool) {
            // kyouko reg
        }
    }

    /**
     * Find the most desirable game instance.
     * @return Possible Game Instance.
     * @since 0.0.1
     */
    fun findGameInstance(): KOTCGame? {
        // TODO: 4/7/2023 implement
        return gamePool.firstOrNull()
    }

    /**
     * Find a game instance by its id.
     * @param id Game Instance id.
     * @return Possible Game Instance.
     * @since 0.0.1
     */
    fun findGameInstanceById(id: Int): KOTCGame? {
        return gamePool.firstOrNull {
            it.id == id
        }
    }
}