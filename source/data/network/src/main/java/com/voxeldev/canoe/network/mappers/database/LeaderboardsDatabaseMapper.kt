package com.voxeldev.canoe.network.mappers.database

import com.voxeldev.canoe.database.objects.CityObject
import com.voxeldev.canoe.database.objects.CurrentUserObject
import com.voxeldev.canoe.database.objects.LanguageObject
import com.voxeldev.canoe.database.objects.LeaderboardEntryObject
import com.voxeldev.canoe.database.objects.LeaderboardsObject
import com.voxeldev.canoe.database.objects.RunningTotalObject
import com.voxeldev.canoe.database.objects.TimeRangeObject
import com.voxeldev.canoe.database.objects.UserObject
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
import io.realm.kotlin.ext.toRealmList

/**
 * @author nvoxel
 */
internal class LeaderboardsDatabaseMapper {

    fun toObject(leaderboardsResponse: LeaderboardsResponse): LeaderboardsObject =
        LeaderboardsObject().apply {
            timestamp = System.currentTimeMillis() / 1000
            currentUser = toObject(leaderboardsResponse.currentUser)
            data = leaderboardsResponse.data.map { toObject(it) }.toRealmList()
            page = leaderboardsResponse.page
            totalPages = leaderboardsResponse.totalPages
            range = toObject(leaderboardsResponse.range)
            language = leaderboardsResponse.language
            isHireable = leaderboardsResponse.isHireable
            countryCode = leaderboardsResponse.countryCode
            modifiedAt = leaderboardsResponse.modifiedAt
            timeout = leaderboardsResponse.timeout
            writesOnly = leaderboardsResponse.writesOnly
        }

    private fun toObject(currentUserResponse: CurrentUserResponse): CurrentUserObject =
        CurrentUserObject().apply {
            rank = currentUserResponse.rank
            runningTotal = currentUserResponse.runningTotal?.let { toObject(it) }
            page = currentUserResponse.page
            user = toObject(currentUserResponse.user)
        }

    private fun toObject(userResponse: UserResponse): UserObject =
        UserObject().apply {
            id = userResponse.id
            email = userResponse.email
            username = userResponse.username
            fullName = userResponse.fullName
            displayName = userResponse.displayName
            website = userResponse.website
            humanReadableWebsite = userResponse.humanReadableWebsite
            isHireable = userResponse.isHireable
            city = userResponse.city?.let { toObject(it) }
            isEmailPublic = userResponse.isEmailPublic
            photoPublic = userResponse.photoPublic
        }

    private fun toObject(cityResponse: CityResponse): CityObject =
        CityObject().apply {
            countryCode = cityResponse.countryCode
            name = cityResponse.name
            state = cityResponse.state
            title = cityResponse.title
        }

    private fun toObject(leaderboardEntryResponse: LeaderboardEntryResponse): LeaderboardEntryObject =
        LeaderboardEntryObject().apply {
            rank = leaderboardEntryResponse.rank
            runningTotal = leaderboardEntryResponse.runningTotal?.let { toObject(it) }
            user = toObject(leaderboardEntryResponse.user)
        }

    private fun toObject(runningTotalResponse: RunningTotalResponse): RunningTotalObject =
        RunningTotalObject().apply {
            totalSeconds = runningTotalResponse.totalSeconds
            humanReadableTotal = runningTotalResponse.humanReadableTotal
            dailyAverage = runningTotalResponse.dailyAverage
            humanReadableDailyAverage = runningTotalResponse.humanReadableDailyAverage
            languages = runningTotalResponse.languages?.map { toObject(it) }?.toRealmList()
        }

    private fun toObject(timeRangeResponse: TimeRangeResponse): TimeRangeObject =
        TimeRangeObject().apply {
            startDate = timeRangeResponse.startDate
            startText = timeRangeResponse.startText
            endDate = timeRangeResponse.endDate
            endText = timeRangeResponse.endText
            name = timeRangeResponse.name
            text = timeRangeResponse.text
        }

    private fun toObject(languageResponse: LanguageResponse): LanguageObject =
        LanguageObject().apply {
            name = languageResponse.name
            totalSeconds = languageResponse.totalSeconds
            percent = languageResponse.percent
            digital = languageResponse.digital
            text = languageResponse.text
            hours = languageResponse.hours
            minutes = languageResponse.minutes
            seconds = languageResponse.seconds
        }

    fun toModel(leaderboardsObject: LeaderboardsObject): LeaderboardsModel =
        LeaderboardsModel(
            currentUser = leaderboardsObject.currentUser?.toModel()!!,
            data = leaderboardsObject.data.map { it.toModel() },
            page = leaderboardsObject.page,
            totalPages = leaderboardsObject.totalPages,
            range = leaderboardsObject.range?.toModel()!!,
            language = leaderboardsObject.language,
            isHireable = leaderboardsObject.isHireable,
            countryCode = leaderboardsObject.countryCode,
            modifiedAt = leaderboardsObject.modifiedAt,
            timeout = leaderboardsObject.timeout,
            writesOnly = leaderboardsObject.writesOnly,
        )

    private fun CurrentUserObject.toModel(): CurrentUser =
        CurrentUser(
            rank = rank,
            page = page,
            runningTotal = runningTotal?.toModel(),
            user = user?.toModel()!!,
        )

    private fun UserObject.toModel(): User =
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

    private fun CityObject.toModel(): City =
        City(
            countryCode = countryCode,
            name = name,
            state = state,
            title = title,
        )

    private fun LeaderboardEntryObject.toModel(): LeaderboardEntry =
        LeaderboardEntry(
            rank = rank,
            runningTotal = runningTotal?.toModel(),
            user = user?.toModel()!!,
        )

    private fun RunningTotalObject.toModel(): RunningTotal =
        RunningTotal(
            totalSeconds = totalSeconds,
            humanReadableTotal = humanReadableTotal,
            dailyAverage = dailyAverage,
            humanReadableDailyAverage = humanReadableDailyAverage,
            languages = languages?.let { languages -> languages.map { it.toModel() } }
        )

    private fun TimeRangeObject.toModel(): TimeRange =
        TimeRange(
            startDate = startDate,
            startText = startText,
            endDate = endDate,
            endText = endText,
            name = name,
            text = text,
        )

    private fun LanguageObject.toModel(): Language =
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
}
