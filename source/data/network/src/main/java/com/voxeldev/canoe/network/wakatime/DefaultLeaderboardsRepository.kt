package com.voxeldev.canoe.network.wakatime

import com.voxeldev.canoe.database.db.leaderboards.LeaderboardsDatabase
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRepository
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import com.voxeldev.canoe.network.base.BaseNetworkRepository
import com.voxeldev.canoe.network.mappers.database.LeaderboardsDatabaseMapper
import com.voxeldev.canoe.network.mappers.network.LeaderboardsMapper
import com.voxeldev.canoe.network.wakatime.datasource.response.LeaderboardsResponse
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.extensions.url
import com.voxeldev.canoe.utils.platform.NetworkHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.realm.kotlin.query.TRUE_PREDICATE

/**
 * @author nvoxel
 */
class DefaultLeaderboardsRepository(
    networkHandler: NetworkHandler,
    httpClient: HttpClient,
    authenticationRepository: AuthenticationRepository,
    private val leaderboardsDatabase: LeaderboardsDatabase,
    private val leaderboardsMapper: LeaderboardsMapper,
    private val leaderboardsDatabaseMapper: LeaderboardsDatabaseMapper,
) : LeaderboardsRepository, BaseNetworkRepository<LeaderboardsModel>(
    networkHandler,
    httpClient,
    authenticationRepository
) {

    override suspend fun getLeaderboards(request: LeaderboardsRequest): Result<LeaderboardsModel> =
        doRequest<LeaderboardsResponse>(
            request = HttpRequestBuilder().apply {
                url(urlString = LEADERBOARDS_URL) {
                    with(request) {
                        language?.let { parameters.append(LANGUAGE_PARAM, language!!) }
                        isHireable?.let { parameters.append(IS_HIREABLE_PARAM, isHireable?.toString()!!) }
                        countryCode?.let { parameters.append(COUNTRY_CODE_PARAM, countryCode!!) }
                        page.let { parameters.append(PAGE_PARAM, page.toString()) }
                    }
                }
            },
            getFromCache = { useOutdatedCache ->
                leaderboardsDatabase.query(
                    query = if (useOutdatedCache) {
                        TRUE_PREDICATE
                    } else {
                        "timestamp > $0 AND language == $1 AND isHireable == $2 AND countryCode == $3"
                    },
                    (System.currentTimeMillis() / 1000) - CACHE_LIFETIME,
                    request.language,
                    request.isHireable ?: false,
                    request.countryCode,
                ) {
                    firstOrNull()?.let { leaderboardsDatabaseMapper.toModel(it) }
                }
            },
            cache = { leaderboardsDatabase.add(leaderboardsDatabaseMapper.toObject(this)) },
            transform = { leaderboardsMapper.toModel(this) }
        )

    private companion object {
        const val LEADERBOARDS_URL = "leaders"
        const val LANGUAGE_PARAM = "language"
        const val IS_HIREABLE_PARAM = "is_hireable"
        const val COUNTRY_CODE_PARAM = "country_code"
        const val PAGE_PARAM = "page"

        const val CACHE_LIFETIME = 3600
    }
}
