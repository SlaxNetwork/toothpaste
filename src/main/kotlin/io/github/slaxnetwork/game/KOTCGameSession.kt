package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.lobby.Lobby
import io.github.slaxnetwork.game.lobby.getLobbyEventNode
import io.github.slaxnetwork.game.player.GamePlayerSession
import io.github.slaxnetwork.game.player.GamePlayerSessionRegistry
import io.github.slaxnetwork.mm
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.EventFilter
import net.minestom.server.event.EventNode
import net.minestom.server.event.instance.AddEntityToInstanceEvent
import net.minestom.server.event.instance.RemoveEntityFromInstanceEvent
import net.minestom.server.event.player.PlayerDisconnectEvent
import net.minestom.server.event.trait.InstanceEvent
import net.minestom.server.instance.AnvilLoader
import net.minestom.server.instance.Instance
import net.minestom.server.timer.Task
import java.nio.file.Path
import java.time.Duration
import java.util.UUID

/**
 * @author Tech
 * @since 0.0.1
 */
class KOTCGameSession(
    val id: Int,
    val instance: Instance
) {
    val players: Set<GamePlayerSession>
        get() = GamePlayerSessionRegistry.players.filter { it.kotcGame.id == id }.toSet()

    val startHandler = StartHandler(this)

    val lobby = Lobby(this, instance)

    var hasStarted: Boolean = false
        private set

    var state: KOTCGameState = KOTCGameState.IN_LOBBY

    val maxConnections: Int get() = MAX_PLAYER_START - players.size
    val acceptingConnections: Boolean get() = maxConnections >= 1 && !hasStarted

    init {
        instance.eventNode()
            .addChild(getLobbyEventNode(lobby))
            .addChild(KOTCGameSessionEventNode.createNode())
    }

    fun startVotePeriod() {

    }

    fun addPlayer(player: Player): GamePlayerSession {
        return GamePlayerSessionRegistry.addPlayer(player, this)
    }

    fun removePlayer(uuid: UUID) {
        GamePlayerSessionRegistry.removePlayer(uuid)
    }

    companion object {
        const val MIN_PLAYER_START = 1
        const val MAX_PLAYER_START = 12

        fun createInstance(): Instance {
            val instance = MinecraftServer.getInstanceManager()
                .createInstanceContainer()

            instance.chunkLoader = AnvilLoader(Path.of("test").toAbsolutePath())

            return instance
        }
    }

    /**
     * @author Tech
     * @since 0.0.1
     */
    class StartHandler(private val kotcGame: KOTCGameSession) {
        private var countdown: Int = SLOW_COUNTDOWN

        private var countdownTask: Task? = null

        val isCountdownActive: Boolean
            get() = countdownTask != null

        /**
         * @since 0.0.1
         */
        fun startGameCountdown(fast: Boolean = false) {
            val potentialCountdown = if(fast) FAST_COUNTDOWN else SLOW_COUNTDOWN

            // TODO: 4/9/2023 might wanna change from -1 default to something else?
            if(potentialCountdown < countdown) {
                countdown = potentialCountdown
            }

            if(!isCountdownActive) {
                countdownTask = buildCountdownTask().schedule()
                // announce timer started.
            }

        }

        /**
         * @since 0.0.1
         */
        private fun buildCountdownTask(): Task.Builder {
            return kotcGame.instance
                .scheduler().buildTask {
                    println("always, $countdown")
                    if(countdown-- == 0) {
                        startVotingPeriod()

                        disposeCountdownTask()
                    }

                    // update bossbar
                }
                .delay(Duration.ofMillis(500))
                .repeat(Duration.ofSeconds(1))
        }

        /**
         * @since 0.0.1
         */
        fun stopGameCountdown() {
            // announce countdown stopped or smth
            disposeCountdownTask()
        }

        /**
         * @since 0.0.1
         */
        private fun disposeCountdownTask() {
            countdownTask?.let { task ->
                task.cancel()
                countdownTask = null
            }

            countdown = SLOW_COUNTDOWN
        }

        /**
         * @since 0.0.1
         */
        fun startVotingPeriod() {
            if(kotcGame.players.size < MIN_PLAYER_START) {
                // don't start game, not enough players, add special handling?
                return
            }

            kotcGame.startVotePeriod()
        }

        /**
         * @since 0.0.1
         */
        private fun updateInstanceBossBar() {

        }

        companion object {
            const val SLOW_COUNTDOWN = 60

            const val FAST_COUNTDOWN = 20
        }
    }
}

/**
 * @author Tech
 * @since 0.0.1
 */
private fun kotcGameSessionNode(kotcGame: KOTCGameSession): EventNode<InstanceEvent> {
    val node = EventNode.type(
        "kotc-game-session-listener",
        EventFilter.INSTANCE
    )

    node.addListener(AddEntityToInstanceEvent::class.java) { ev ->
        val player = ev.entity as? Player
            ?: return@addListener

        // in session
        if(kotcGame.hasStarted) {
            player.respawnPoint = Pos.ZERO
        // in lobby
        } else {
            player.respawnPoint = Pos(0.0, 103.0, 0.0)
        }

        player.gameMode = GameMode.CREATIVE
    }

    node.addListener(RemoveEntityFromInstanceEvent::class.java) { ev ->
        val player = ev.entity as? Player
            ?: return@addListener
    }

    node.addListener(PlayerDisconnectEvent::class.java) { ev ->
        val player = ev.player

        if(kotcGame.hasStarted) {
            GamePlayerSessionRegistry.findPlayer(player.uuid)
                ?.connected = false
        } else {
            GamePlayerSessionRegistry.removePlayer(player.uuid)
        }
    }

    return node
}