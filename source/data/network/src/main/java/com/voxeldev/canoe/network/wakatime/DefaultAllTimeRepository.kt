package com.voxeldev.canoe.network.wakatime

import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRepository
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRequest
import com.voxeldev.canoe.database.db.alltime.AllTimeDatabase
import com.voxeldev.canoe.network.base.BaseNetworkRepository
import com.voxeldev.canoe.network.mappers.database.AllTimeDatabaseMapper
import com.voxeldev.canoe.network.mappers.network.AllTimeMapper
import com.voxeldev.canoe.network.wakatime.datasource.response.AllTimeResponse
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.extensions.url
import com.voxeldev.canoe.utils.platform.NetworkHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.realm.kotlin.query.TRUE_PREDICATE

/**
 * @author nvoxel
 */
internal class DefaultAllTimeRepository(
    networkHandler: NetworkHandler,
    httpClient: HttpClient,
    authenticationRepository: AuthenticationRepository,
    private val allTimeDatabase: AllTimeDatabase,
    private val allTimeMapper: AllTimeMapper,
    private val allTimeDatabaseMapper: AllTimeDatabaseMapper,
) : AllTimeRepository, BaseNetworkRepository<AllTimeModel>(
    networkHandler,
    httpClient,
    authenticationRepository,
) {

    override suspend fun getAllTime(request: AllTimeRequest): Result<AllTimeModel> =
        doRequest<AllTimeResponse>(
            request = HttpRequestBuilder().apply {
                url(urlString = ALL_TIME_URL) {
                    request.project?.let { parameters.append(PROJECT_PARAM, it) }
                }
            },
            getFromCache = { useOutdatedCache ->
                allTimeDatabase.query(
                    query = if (useOutdatedCache) {
                        TRUE_PREDICATE
                    } else {
                        "timestamp > $0 AND project = $1"
                    },
                    (System.currentTimeMillis() / 1000) - CACHE_LIFETIME,
                    request.project,
                ) {
                    firstOrNull()?.let { allTimeDatabaseMapper.toModel(it) }
                }
            },
            cache = {
                allTimeDatabase.add(
                    allTimeDatabaseMapper.toObject(allTimeResponse = this, projectName = request.project),
                )
            },
            transform = { allTimeMapper.toModel(this) },
        )

    private companion object {
        const val ALL_TIME_URL = "users/current/all_time_since_today"
        const val PROJECT_PARAM = "project"

        const val CACHE_LIFETIME = 300
    }
}
