package com.voxeldev.canoe.dashboard.integration

import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRepository
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRequest
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetAllTimeUseCase : BaseUseCase<AllTimeRequest, AllTimeModel>(), KoinComponent {

    private val allTimeRepository: AllTimeRepository by inject()

    override suspend fun run(params: AllTimeRequest): Result<AllTimeModel> =
        allTimeRepository.getAllTime(params)
}
