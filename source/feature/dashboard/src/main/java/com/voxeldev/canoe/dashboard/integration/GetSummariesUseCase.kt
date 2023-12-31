package com.voxeldev.canoe.dashboard.integration

import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRepository
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRequest
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetSummariesUseCase : BaseUseCase<SummariesRequest, SummariesModel>(), KoinComponent {

    private val summariesRepository: SummariesRepository by inject()

    override suspend fun run(params: SummariesRequest): Result<SummariesModel> =
        summariesRepository.getSummaries(params)
}
