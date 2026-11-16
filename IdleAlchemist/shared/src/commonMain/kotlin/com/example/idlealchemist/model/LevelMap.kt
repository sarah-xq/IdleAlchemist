package com.example.idlealchemist.model

/**
 * Represents a world map containing an ordered series of levels.
 *
 * @property id Internal ID of the map (int). It cannot be accessed or modified by the user.
 *              This design enables the creation of multiple world maps, making the system scalable
 *              for different themed maps as well as debug maps for testing purposes.
 * @property worldName Display name of the world map (string). For display purposes only;
 *                      cannot be modified by the user and does not have to be unique.
 * @property levels Ordered list of all levels in this map. Used to load levels into the world map.
 *                  Users can interact with individual levels but cannot modify the level order or
 *                  access locked levels.
 */
data class LevelMap(
    val id: Int,
    val worldName: String,
    val levels: List<TrailStage>,
)
