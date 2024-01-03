package com.voxeldev.canoe.network.wakatime

import com.voxeldev.canoe.database.db.projects.ProjectsDatabase
import com.voxeldev.canoe.network.base.BaseNetworkRepository
import com.voxeldev.canoe.network.mappers.database.ProjectsDatabaseMapper
import com.voxeldev.canoe.network.mappers.network.ProjectsMapper
import com.voxeldev.canoe.network.wakatime.datasource.response.ProjectsResponse
import com.voxeldev.canoe.projects.api.ProjectsModel
import com.voxeldev.canoe.projects.api.ProjectsRepository
import com.voxeldev.canoe.projects.api.ProjectsRequest
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.extensions.url
import com.voxeldev.canoe.utils.platform.NetworkHandler
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.realm.kotlin.query.TRUE_PREDICATE

/**
 * @author nvoxel
 */
internal class DefaultProjectsRepository(
    networkHandler: NetworkHandler,
    httpClient: HttpClient,
    authenticationRepository: AuthenticationRepository,
    private val projectsDatabase: ProjectsDatabase,
    private val projectsMapper: ProjectsMapper,
    private val projectsDatabaseMapper: ProjectsDatabaseMapper,
) : ProjectsRepository, BaseNetworkRepository<ProjectsModel>(networkHandler, httpClient, authenticationRepository) {

    override suspend fun getProjects(request: ProjectsRequest): Result<ProjectsModel> =
        doRequest<ProjectsResponse>(
            request = HttpRequestBuilder().apply {
                url(urlString = PROJECTS_URL) {
                    request.searchQuery?.let { searchQuery -> parameters.append(Q_PARAM, searchQuery) }
                }
            },
            getFromCache = { useOutdatedCache ->
                projectsDatabase.query(
                    query = if (useOutdatedCache) TRUE_PREDICATE else "timestamp > $0 AND query = $1",
                    (System.currentTimeMillis() / 1000) - CACHE_LIFETIME,
                    request.searchQuery,
                ) {
                    firstOrNull()?.let { projectsDatabaseMapper.toModel(it) }
                }
            },
            cache = {
                projectsDatabase.add(
                    projectsDatabaseMapper.toObject(projectsResponse = this, searchQuery = request.searchQuery),
                )
            },
            transform = { projectsMapper.toModel(this) },
        )

    private companion object {
        const val PROJECTS_URL = "users/current/projects"
        const val Q_PARAM = "q"

        const val CACHE_LIFETIME = 1800
    }
}
