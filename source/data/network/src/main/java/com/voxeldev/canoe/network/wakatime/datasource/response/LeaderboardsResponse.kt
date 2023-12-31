package com.voxeldev.canoe.network.wakatime.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author nvoxel
 */
@Serializable
internal data class LeaderboardsResponse(
    @SerialName("current_user")
    val currentUser: CurrentUserResponse,
    val data: List<LeaderboardEntryResponse>,
    val page: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    val range: TimeRangeResponse,
    val language: String? = null,
    @SerialName("is_hireable")
    val isHireable: Boolean? = null,
    @SerialName("country_code")
    val countryCode: String? = null,
    @SerialName("modified_at")
    val modifiedAt: String,
    val timeout: Int,
    @SerialName("writes_only")
    val writesOnly: Boolean,
)

@Serializable
internal data class CurrentUserResponse(
    val rank: Int?,
    @SerialName("running_total")
    val runningTotal: RunningTotalResponse?,
    val page: Int?,
    val user: UserResponse,
)

@Serializable
internal data class UserResponse(
    val id: String,
    val email: String?,
    val username: String? = null,
    @SerialName("full_name")
    val fullName: String? = null,
    @SerialName("display_name")
    val displayName: String? = null,
    val website: String? = null,
    @SerialName("human_readable_website")
    val humanReadableWebsite: String? = null,
    @SerialName("is_hireable")
    val isHireable: Boolean? = null,
    val city: CityResponse? = null,
    @SerialName("is_email_public")
    val isEmailPublic: Boolean? = null,
    @SerialName("photo_public")
    val photoPublic: Boolean? = null,
)

@Serializable
internal data class CityResponse(
    @SerialName("country_code")
    val countryCode: String? = null,
    val name: String? = null,
    val state: String? = null,
    val title: String? = null,
)

@Serializable
internal data class LeaderboardEntryResponse(
    val rank: Int,
    @SerialName("running_total")
    val runningTotal: RunningTotalResponse? = null,
    val user: UserResponse,
)

@Serializable
internal data class RunningTotalResponse(
    @SerialName("total_seconds")
    val totalSeconds: Float? = null,
    @SerialName("human_readable_total")
    val humanReadableTotal: String? = null,
    @SerialName("daily_average")
    val dailyAverage: Float? = null,
    @SerialName("human_readable_daily_average")
    val humanReadableDailyAverage: String? = null,
    val languages: List<LanguageResponse>? = null,
)

@Serializable
internal data class TimeRangeResponse(
    @SerialName("start_date")
    val startDate: String? = null,
    @SerialName("start_text")
    val startText: String? = null,
    @SerialName("end_date")
    val endDate: String? = null,
    @SerialName("end_text")
    val endText: String? = null,
    val name: String? = null,
    val text: String? = null,
)
