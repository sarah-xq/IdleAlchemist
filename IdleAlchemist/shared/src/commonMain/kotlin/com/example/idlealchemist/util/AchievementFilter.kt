package com.example.idlealchemist.util

import com.example.idlealchemist.model.AchievementStatus

enum class AchievementSort(val label: String) {
    Default("Default"),
    Name("A-Z"),
    Progress("Progress")
}

/**
 * Applies the search box text. The words "owned" and "unowned" are keywords that keep only owned
 * or only unowned achievements. Every other word must appear in the title or description.
 * Example: "unowned click" = achievements not yet earned that mention "click".
 */
fun filterAchievements(items: List<AchievementStatus>, query: String): List<AchievementStatus> {
    var ownedOnly: Boolean? = null
    val words = mutableListOf<String>()
    for (token in query.lowercase().split(' ').filter { it.isNotBlank() }) {
        when (token) {
            "owned" -> ownedOnly = true
            "unowned" -> ownedOnly = false
            else -> words += token
        }
    }
    return items.filter { s ->
        val text = (s.achievement.title + " " + s.achievement.description).lowercase()
        (ownedOnly == null || s.owned == ownedOnly) && words.all { it in text }
    }
}

fun sortAchievements(items: List<AchievementStatus>, sort: AchievementSort): List<AchievementStatus> =
    when (sort) {
        AchievementSort.Default -> items
        AchievementSort.Name -> items.sortedBy { it.achievement.title.lowercase() }
        AchievementSort.Progress -> items.sortedByDescending {
            it.progress.toFloat() / it.achievement.requirement.target
        }
    }
