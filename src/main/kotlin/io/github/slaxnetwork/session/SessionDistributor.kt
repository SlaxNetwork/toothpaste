package io.github.slaxnetwork.session

import io.github.slaxnetwork.game.KOTCGameSession

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

    private val GAME_POOL = mutableSetOf<KOTCGameSession>()
    val gamePool: Set<KOTCGameSession>
        get() = GAME_POOL

    /**
     * Create a pool of available game sessions that are a part
     * of this server.
     * @since 0.0.1
     */
    suspend fun addToSessionPool(amount: Int) {
        // dont go over allocation amount.
        if((gamePool.size + amount) > MAX_GAME_SESSIONS) {
            return
        }

        repeat(amount) {
            val kotcGame = KOTCGameSession(it)

            GAME_POOL.add(kotcGame)
        }

        registerServerPoolToKyouko()
    }

    /**
     * Register the server pool onto Kyouko.
     * @since 0.0.1
     */
    private suspend fun registerServerPoolToKyouko() {
        for(gameInstance in gamePool) {
            // kyouko reg
        }
    }

    /**
     * @since 0.0.1
     */
    private suspend fun unregisterFromKyouko(kotcGame: KOTCGameSession) {

    }

    /**
     * Find the most desirable [KOTCGameSession].
     * @return Possible Game Instance.
     * @since 0.0.1
     */
    fun findGameSession(): KOTCGameSession? {
        // TODO: 4/7/2023 implement
        return gamePool.firstOrNull()
//        return gamePool.filter { it.acceptingConnections }
//            .minByOrNull { it.players.size }
    }

    /**
     * Find a [KOTCGameSession] by its id.
     * @param id Game Instance id.
     * @return Possible Game Instance.
     * @since 0.0.1
     */
    fun findGameSessionById(id: String): KOTCGameSession? {
        return gamePool.firstOrNull()
//        return gamePool.firstOrNull {
//            it.id == id
//        }
    }
}