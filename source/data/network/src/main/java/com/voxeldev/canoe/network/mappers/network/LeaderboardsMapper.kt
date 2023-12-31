package com.voxeldev.canoe.network.mappers.network

import com.voxeldev.canoe.leaderboards.api.City
import com.voxeldev.canoe.leaderboards.api.CurrentUser
import com.voxeldev.canoe.leaderboards.api.Language
import com.voxeldev.canoe.leaderboards.api.LeaderboardEntry
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.RunningTotal
import com.voxeldev.canoe.leaderboards.api.TimeRange
import com.voxeldev.canoe.leaderboards.api.User
import com.voxeldev.canoe.network.wakatime.datasource.response.CityResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.CurrentUserResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.LanguageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.LeaderboardEntryResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.LeaderboardsResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.RunningTotalResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.TimeRangeResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.UserResponse

/**
 * @author nvoxel
 */
internal class LeaderboardsMapper {

    fun toModel(leaderboardsResponse: LeaderboardsResponse): LeaderboardsModel =
        LeaderboardsModel(
            currentUser = leaderboardsResponse.currentUser.toModel(),
            data = leaderboardsResponse.data.map { it.toModel() },
            page = leaderboardsResponse.page,
            totalPages = leaderboardsResponse.totalPages,
            range = leaderboardsResponse.range.toModel(),
            language = leaderboardsResponse.language,
            isHireable = leaderboardsResponse.isHireable,
            countryCode = leaderboardsResponse.countryCode,
            modifiedAt = leaderboardsResponse.modifiedAt,
            timeout = leaderboardsResponse.timeout,
            writesOnly = leaderboardsResponse.writesOnly,
        )

    private fun CurrentUserResponse.toModel(): CurrentUser =
        CurrentUser(
            rank = rank,
            page = page,
            runningTotal = runningTotal?.toModel(),
            user = user.toModel(),
        )

    private fun UserResponse.toModel(): User =
        User(
            id = id,
            email = email,
            username = username,
            fullName = fullName,
            displayName = displayName,
            website = website,
            humanReadableWebsite = humanReadableWebsite,
            isHireable = isHireable,
            city = city?.toModel(),
            isEmailPublic = isEmailPublic,
            photoPublic = photoPublic,
        )

    private fun CityResponse.toModel(): City =
        City(
            countryCode = countryCode,
            name = name,
            state = state,
            title = title,
        )

    private fun LeaderboardEntryResponse.toModel(): LeaderboardEntry =
        LeaderboardEntry(
            rank = rank,
            runningTotal = runningTotal?.toModel(),
            user = user.toModel(),
        )

    private fun RunningTotalResponse.toModel(): RunningTotal =
        RunningTotal(
            totalSeconds = totalSeconds,
            humanReadableTotal = humanReadableTotal,
            dailyAverage = dailyAverage,
            humanReadableDailyAverage = humanReadableDailyAverage,
            languages = languages?.map { it.toModel() },
        )

    private fun LanguageResponse.toModel(): Language =
        Language(
            name = name,
            totalSeconds = totalSeconds,
            percent = percent,
            digital = digital,
            text = text,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
        )

    private fun TimeRangeResponse.toModel(): TimeRange =
        TimeRange(
            startDate = startDate,
            startText = startText,
            endDate = endDate,
            endText = endText,
            name = name,
            text = text,
        )
}
