package com.voxeldev.canoe.network.wakatime

import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesRepository
import com.voxeldev.canoe.database.db.languages.ProgramLanguagesDatabase
import com.voxeldev.canoe.network.base.BaseNetworkRepository
import com.voxeldev.canoe.network.mappers.database.ProgramLanguagesDatabaseMapper
import com.voxeldev.canoe.network.mappers.network.ProgramLanguagesMapper
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguagesResponse
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.platform.NetworkHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.realm.kotlin.query.TRUE_PREDICATE

/**
 * @author nvoxel
 */
internal class DefaultProgramLanguagesRepository(
    networkHandler: NetworkHandler,
    httpClient: HttpClient,
    authenticationRepository: AuthenticationRepository,
    private val programLanguagesDatabase: ProgramLanguagesDatabase,
    private val programLanguagesMapper: ProgramLanguagesMapper,
    private val programLanguagesDatabaseMapper: ProgramLanguagesDatabaseMapper,
) : ProgramLanguagesRepository, BaseNetworkRepository<ProgramLanguagesModel>(
    networkHandler,
    httpClient,
    authenticationRepository,
) {

    override suspend fun getProgramLanguages(): Result<ProgramLanguagesModel> =
        doRequest<ProgramLanguagesResponse>(
            request = HttpRequestBuilder().apply {
                url(urlString = PROGRAM_LANGUAGES_URL)
            },
            getFromCache = { useOutdatedCache ->
                programLanguagesDatabase.query(
                    query = if (useOutdatedCache) TRUE_PREDICATE else "timestamp > $0",
                    (System.currentTimeMillis() / 1000) - CACHE_LIFETIME,
                ) {
                    firstOrNull()?.let { programLanguagesDatabaseMapper.toModel(it) }
                }
            },
            cache = { programLanguagesDatabase.add(programLanguagesDatabaseMapper.toObject(this)) },
            transform = { programLanguagesMapper.toModel(this) },
        )

    private companion object {
        const val PROGRAM_LANGUAGES_URL = "program_languages"

        const val CACHE_LIFETIME = 86400
    }
}
