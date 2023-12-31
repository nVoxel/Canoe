package com.voxeldev.canoe.dashboard.integration

import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesRepository
import com.voxeldev.canoe.utils.integration.BaseUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class GetProgramLanguagesUseCase : BaseUseCase<BaseUseCase.NoParams, ProgramLanguagesModel>(), KoinComponent {

    private val programLanguagesRepository: ProgramLanguagesRepository by inject()

    override suspend fun run(params: NoParams): Result<ProgramLanguagesModel> =
        programLanguagesRepository.getProgramLanguages()
}
