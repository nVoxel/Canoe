package com.voxeldev.canoe.dashboard.api.languages

/**
 * @author nvoxel
 */
interface ProgramLanguagesRepository {

    suspend fun getProgramLanguages(): Result<ProgramLanguagesModel>
}
