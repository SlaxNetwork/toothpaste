package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.lobby.Lobby
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import net.minestom.server.event.EventNode
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.Instance
import net.minestom.server.timer.Scheduler
import net.minestom.server.timer.Task
import java.time.Duration

/**
 * @author Tech
 * @since 0.0.1
 */
class KOTCGameSession(val id: Int) {
    /**
     * [Lobby] instance attached to the [KOTCGameSession].
     */
    val lobby = Lobby(this)

    val startHandler = StartHandler(currentInstance.scheduler())

    /**
     * Current [MicroGame] being played on the [KOTCGameSession].
     */
    var microGame: MicroGame? = null
        private set

    /**
     * State of the [KOTCGameSession].
     */
    var state: KOTCGameState = KOTCGameState.IN_LOBBY

    /**
     * Current [Instance] of the [KOTCGameSession].
     * This will default to the instance of the [MicroGame] if
     * it's not null and will be of the [Lobby] if a [MicroGame] is
     * not being played.
     */
    val currentInstance: Instance
        get() = microGame?.map?.instance
            ?: lobby.instance
            ?: run {
                // try to load the lobby as a fallback if our instances are mis-managed.
                println("attempting to load lobby instance as session instance is mis-managed.")
                lobby.load()
            }
            ?: throw NullPointerException("no instance was able to be loaded in for the session.")

    /**
     * Referencing [EventNode] of the current active [Instance].
     */
    val instanceEventNode: EventNode<InstanceEvent>
        get() = currentInstance.eventNode()

    /**
     * Whether the [KOTCGameSession] has started.
     * If true no new connections will be accepted into the session.
     */
    var hasStarted: Boolean = false
        private set

    fun startMicroGame() {

    }

    fun endMicroGame() {

    }

    /**
     * @author Tech
     * @since 0.0.1
     */
    class StartHandler(private val scheduler: Scheduler) {
        private var countdown: Countdown? = null

        val isCountdownActive: Boolean
            get() = countdown != null

        /**
         * @since 0.0.1
         */
        fun startCountdown(fast: Boolean = false) {
            val timer = if(fast) FAST_COUNTDOWN else SLOW_COUNTDOWN

            if(isCountdownActive) {
                countdown?.let {
                    if(timer > it.timer) {
                        return
                    }

                    it.timer = timer
                }

                return
            } else {
                val countdownTask = buildCountdownTask()
                    .schedule()

                countdown = Countdown(timer, countdownTask)
            }
        }

        /**
         * @since 0.0.1
         */
        private fun buildCountdownTask(): Task.Builder {
            return scheduler
                .buildTask {
                    countdown?.let {
                        if(it.timer-- == 0) {
                            // start game & dispose.
                            disposeCountdown()
                        }

                        // update bossbar.
                    }
                }
                .delay(Duration.ofMillis(500))
                .repeat(Duration.ofSeconds(1))
        }

        /**
         * @since 0.0.1
         */
        fun stopCountdown() {
            disposeCountdown()
        }

        /**
         * @since 0.0.1
         */
        private fun disposeCountdown() {
            countdown?.let {
                it.task.cancel()

                countdown = null
            }
        }

        /**
         * @author Tech
         * @since 0.0.1
         */
        private data class Countdown(
            var timer: Int,
            val task: Task
        )

        companion object {
            private const val SLOW_COUNTDOWN = 60

            private const val FAST_COUNTDOWN = 20
        }
    }
}