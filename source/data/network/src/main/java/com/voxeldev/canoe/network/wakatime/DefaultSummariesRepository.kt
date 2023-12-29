package com.voxeldev.canoe.network.wakatime

import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRepository
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRequest
import com.voxeldev.canoe.database.db.summaries.SummariesDatabase
import com.voxeldev.canoe.network.base.BaseNetworkRepository
import com.voxeldev.canoe.network.mappers.database.SummariesDatabaseMapper
import com.voxeldev.canoe.network.mappers.network.SummariesMapper
import com.voxeldev.canoe.network.wakatime.datasource.response.SummariesResponse
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.extensions.url
import com.voxeldev.canoe.utils.platform.NetworkHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.realm.kotlin.query.TRUE_PREDICATE
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * @author nvoxel
 */
class DefaultSummariesRepository(
    networkHandler: NetworkHandler,
    httpClient: HttpClient,
    authenticationRepository: AuthenticationRepository,
    private val summariesDatabase: SummariesDatabase,
    private val summariesMapper: SummariesMapper,
    private val summariesDatabaseMapper: SummariesDatabaseMapper,
) : SummariesRepository, BaseNetworkRepository<SummariesModel>(networkHandler, httpClient, authenticationRepository) {

    override suspend fun getSummaries(request: SummariesRequest): Result<SummariesModel> =
        doRequest<SummariesResponse>(
            request = HttpRequestBuilder().apply {
                url(urlString = SUMMARIES_URL) {
                    with(request) {
                        parameters.append(START_DATE_PARAM, startDate)
                        parameters.append(END_DATE_PARAM, endDate)
                        project?.let { parameters.append(PROJECT_PARAM, project!!) }
                        branches?.let { parameters.append(BRANCHES_PARAM, branches!!) }
                        timeout?.let { parameters.append(TIMEOUT_PARAM, timeout.toString()) }
                        writesOnly?.let { parameters.append(WRITES_ONLY_PARAM, writesOnly.toString()) }
                        timeZone?.let { parameters.append(TIME_ZONE_PARAM, timeZone!!) }
                    }
                }
            },
            getFromCache = { useOutdatedCache ->
                summariesDatabase.query(
                    query = if (useOutdatedCache) {
                        TRUE_PREDICATE
                    } else {
                        "timestamp > $0 AND start == $1 AND end = $2"
                    },
                    (System.currentTimeMillis() / 1000) - CACHE_LIFETIME,
                    request.startDate.toUnixTimestamp(),
                    request.endDate.toUnixTimestamp(),
                ) {
                    firstOrNull()?.let { summariesDatabaseMapper.toModel(it) }
                }
            },
            cache = {
                summariesDatabase.add(summariesDatabaseMapper.toObject(this))
            },
            transform = { summariesMapper.toModel(this) }
        )

    private fun String.toUnixTimestamp(): Long =
        LocalDate.parse(this, DateTimeFormatter.ISO_DATE).atStartOfDay().toEpochSecond(ZoneOffset.UTC)

    private companion object {
        const val SUMMARIES_URL = "users/current/summaries"
        const val START_DATE_PARAM = "start"
        const val END_DATE_PARAM = "end"
        const val PROJECT_PARAM = "project"
        const val BRANCHES_PARAM = "branches"
        const val TIMEOUT_PARAM = "timeout"
        const val WRITES_ONLY_PARAM = "writes_only"
        const val TIME_ZONE_PARAM = "timezone"

        const val CACHE_LIFETIME = 300
    }
}
