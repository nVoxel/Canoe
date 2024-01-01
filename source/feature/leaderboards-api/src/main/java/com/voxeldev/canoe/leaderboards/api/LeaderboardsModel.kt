package com.voxeldev.canoe.leaderboards.api

/**
 * @author nvoxel
 */
data class LeaderboardsModel(
    val currentUser: CurrentUser,
    val data: List<LeaderboardEntry>,
    val page: Int,
    val totalPages: Int,
    val range: TimeRange,
    val language: String? = null,
    val isHireable: Boolean? = null,
    val countryCode: String? = null,
    val modifiedAt: String,
    val timeout: Int,
    val writesOnly: Boolean,
)

abstract class GeneralizedUser {
    abstract val rank: Int?
    abstract val runningTotal: RunningTotal?
    abstract val user: User
}

data class CurrentUser(
    override val rank: Int? = null,
    val page: Int? = null,
    override val runningTotal: RunningTotal? = null,
    override val user: User,
) : GeneralizedUser()

data class User(
    val id: String,
    val email: String? = null,
    val username: String? = null,
    val fullName: String? = null,
    val displayName: String? = null,
    val website: String? = null,
    val humanReadableWebsite: String? = null,
    val isHireable: Boolean? = null,
    val city: City? = null,
    val isEmailPublic: Boolean? = null,
    val photoPublic: Boolean? = null,
)

data class City(
    val countryCode: String? = null,
    val name: String? = null,
    val state: String? = null,
    val title: String? = null,
)

data class LeaderboardEntry(
    override val rank: Int,
    override val runningTotal: RunningTotal? = null,
    override val user: User,
) : GeneralizedUser()

data class RunningTotal(
    val totalSeconds: Float? = null,
    val humanReadableTotal: String? = null,
    val dailyAverage: Float? = null,
    val humanReadableDailyAverage: String? = null,
    val languages: List<Language>? = null,
)

data class Language(
    val name: String,
    val totalSeconds: Float,
    val percent: Float? = null,
    val digital: String? = null,
    val text: String? = null,
    val hours: Int? = null,
    val minutes: Int? = null,
    val seconds: Int? = null,
)

data class TimeRange(
    val startDate: String? = null,
    val startText: String? = null,
    val endDate: String? = null,
    val endText: String? = null,
    val name: String? = null,
    val text: String? = null,
)
