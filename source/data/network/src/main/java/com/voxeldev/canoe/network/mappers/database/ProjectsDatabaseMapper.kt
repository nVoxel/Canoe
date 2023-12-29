package com.voxeldev.canoe.network.mappers.database

import com.voxeldev.canoe.database.objects.ProjectObject
import com.voxeldev.canoe.database.objects.ProjectsObject
import com.voxeldev.canoe.network.wakatime.datasource.response.ProjectResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.ProjectsResponse
import com.voxeldev.canoe.projects.api.Project
import com.voxeldev.canoe.projects.api.ProjectsModel
import io.realm.kotlin.ext.toRealmList

/**
 * @author nvoxel
 */
class ProjectsDatabaseMapper {

    fun toObject(projectsResponse: ProjectsResponse, searchQuery: String?): ProjectsObject =
        ProjectsObject().apply {
            timestamp = System.currentTimeMillis() / 1000
            data = projectsResponse.data.map { toObject(it) }.toRealmList()
            query = searchQuery
            message = projectsResponse.message
        }

    private fun toObject(projectResponse: ProjectResponse): ProjectObject =
        ProjectObject().apply {
            id = projectResponse.id
            name = projectResponse.name
            repository = projectResponse.repository
            badge = projectResponse.badge
            color = projectResponse.color
            hasPublicUrl = projectResponse.hasPublicUrl
            humanReadableLastHeartBeatAt = projectResponse.humanReadableLastHeartBeatAt
            lastHeartBeatAt = projectResponse.lastHeartBeatAt
            url = projectResponse.url
            urlEncodedName = projectResponse.urlEncodedName
            createdAt = projectResponse.createdAt
        }

    fun toModel(projectsObject: ProjectsObject): ProjectsModel =
        ProjectsModel(
            data = projectsObject.data.map { it.toModel() },
            message = projectsObject.message,
        )

    private fun ProjectObject.toModel(): Project =
        Project(
            id = id,
            name = name,
            repository = repository,
            badge = badge,
            color = color,
            hasPublicUrl = hasPublicUrl,
            humanReadableLastHeartBeatAt = humanReadableLastHeartBeatAt,
            lastHeartBeatAt = lastHeartBeatAt,
            url = url,
            urlEncodedName = urlEncodedName,
            createdAt = createdAt,
        )
}
