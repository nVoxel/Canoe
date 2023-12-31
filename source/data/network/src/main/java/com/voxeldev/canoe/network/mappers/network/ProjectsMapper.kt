package com.voxeldev.canoe.network.mappers.network

import com.voxeldev.canoe.network.wakatime.datasource.response.ProjectResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.ProjectsResponse
import com.voxeldev.canoe.projects.api.Project
import com.voxeldev.canoe.projects.api.ProjectsModel

/**
 * @author nvoxel
 */
internal class ProjectsMapper {

    fun toModel(projectsResponse: ProjectsResponse): ProjectsModel =
        ProjectsModel(
            data = projectsResponse.data.map { it.toModel() },
            message = projectsResponse.message,
        )

    private fun ProjectResponse.toModel(): Project =
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
