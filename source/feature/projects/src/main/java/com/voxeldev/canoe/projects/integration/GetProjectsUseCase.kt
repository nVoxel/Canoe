package com.voxeldev.canoe.projects.integration

import com.voxeldev.canoe.projects.api.ProjectsModel
import com.voxeldev.canoe.projects.api.ProjectsRepository
import com.voxeldev.canoe.projects.api.ProjectsRequest
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetProjectsUseCase : BaseUseCase<ProjectsRequest, ProjectsModel>(), KoinComponent {

    private val projectsRepository: ProjectsRepository by inject()

    override suspend fun run(params: ProjectsRequest): Result<ProjectsModel> =
        projectsRepository.getProjects(params)
}
