package com.example.idlealchemist.model

/**
 * Everything saved for one stage of one alchemist.
 *
 * Each stage owns a separate clicker [game], so points, upgrades, and
 * apprentices start from zero in every new stage and are kept when the
 * player leaves and later returns.
 *
 * @property game The stage's own clicker game.
 * @property recentTapTimes Times of taps still inside the combo window,
 * in milliseconds. Only used by [StageGoal.Combo] stages.
 * @property bestCombo Most taps landed inside one combo window so far.
 * @property completed Whether the goal has been reached. Stays true after
 * replays, so the next stage never locks again.
 */
data class LevelProgress(
    val game: GameState = GameState(),
    val recentTapTimes: List<Long> = emptyList(),
    val bestCombo: Int = 0,
    val completed: Boolean = false,
)

/** Where a stage stands for the player. */
enum class StageStatus {
    LOCKED,
    UNLOCKED,
    COMPLETED,
}

/**
 * How far the stage's goal has progressed, capped at the goal's target.
 *
 * @param goal Requirement of the stage this progress belongs to.
 */
fun LevelProgress.goalProgress(goal: StageGoal): Int {
    val reached = when (goal) {
        is StageGoal.Taps -> game.stats.manualClicks
        is StageGoal.Combo -> bestCombo
    }
    return reached.coerceAtMost(goal.target)
}

/**
 * Applies one tap at [nowMillis]: the stage's clicker gains essence, the combo
 * window moves forward, and the stage completes once its goal is reached.
 *
 * @param goal Requirement of the stage being played.
 * @param nowMillis Current time in milliseconds from any fixed starting point.
 */
fun LevelProgress.afterTap(goal: StageGoal, nowMillis: Long): LevelProgress {
    val tapped = when (goal) {
        is StageGoal.Taps -> copy(game = game.afterTap())
        is StageGoal.Combo -> {
            val windowStart = nowMillis - goal.windowSeconds * 1000L
            val inWindow = recentTapTimes.filter { time -> time > windowStart } + nowMillis
            copy(
                game = game.afterTap(),
                recentTapTimes = inWindow,
                bestCombo = maxOf(bestCombo, inWindow.size),
            )
        }
    }
    return tapped.copy(completed = completed || tapped.goalProgress(goal) >= goal.target)
}

/**
 * Decides whether each stage can be played: the first stage always can,
 * and every later stage needs the stage before it completed.
 *
 * @param stages Stages in trail order.
 * @param levels Saved progress keyed by stage number.
 * @return Status of every stage, keyed by stage number.
 */
fun stageStatuses(
    stages: List<TrailStage>,
    levels: Map<Int, LevelProgress>,
): Map<Int, StageStatus> = stages.withIndex().associate { (index, stage) ->
    val previousCompleted = index == 0 ||
            levels[stages[index - 1].number]?.completed == true

    stage.number to when {
        levels[stage.number]?.completed == true -> StageStatus.COMPLETED
        previousCompleted -> StageStatus.UNLOCKED
        else -> StageStatus.LOCKED
    }
}
