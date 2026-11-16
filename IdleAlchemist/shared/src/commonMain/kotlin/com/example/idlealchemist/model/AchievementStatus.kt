package com.example.idlealchemist.model

/** An achievement together with the player's progress on it, ready to display. */
data class AchievementStatus(
    val achievement: Achievement,
    val progress: Int,
    val owned: Boolean
)

/** Progress toward [a], counted from the last time it was deleted, capped at the target. */
fun GameState.progressOf(a: Achievement): Int =
    (a.requirement.stat(stats) - (baselines[a.id] ?: 0)).coerceIn(0, a.requirement.target)

fun GameState.achievementStatuses(): List<AchievementStatus> =
    Achievements.all.map { a ->
        val owned = a.id in ownedAchievements
        AchievementStatus(a, if (owned) a.requirement.target else progressOf(a), owned)
    }

/** Awards any achievement whose goal has just been reached and sets the unlock message. */
fun GameState.withUnlocks(): GameState {
    val newly = Achievements.all.filter {
        it.id !in ownedAchievements && progressOf(it) >= it.requirement.target
    }
    if (newly.isEmpty()) return this
    return copy(
        ownedAchievements = ownedAchievements + newly.map { it.id },
        unlockNotice = UnlockNotice(newly.first().title, 4)
    )
}
