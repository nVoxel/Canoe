package com.voxeldev.canoe.projects.api

/**
 * @author nvoxel
 */
interface ProjectsRepository {

    suspend fun getProjects(request: ProjectsRequest): Result<ProjectsModel>
}
