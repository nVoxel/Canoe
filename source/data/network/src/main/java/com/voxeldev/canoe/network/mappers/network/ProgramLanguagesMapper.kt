package com.voxeldev.canoe.network.mappers.network

import android.graphics.Color
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguage
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguageResponse
import com.voxeldev.canoe.network.wakatime.datasource.response.ProgramLanguagesResponse
import com.voxeldev.canoe.utils.extensions.toColorInt

/**
 * @author nvoxel
 */
class ProgramLanguagesMapper {

    fun toModel(programLanguagesResponse: ProgramLanguagesResponse): ProgramLanguagesModel =
        ProgramLanguagesModel(
            data = programLanguagesResponse.data.map { it.toModel() },
            total = programLanguagesResponse.total,
            totalPages = programLanguagesResponse.totalPages,
        )

    private fun ProgramLanguageResponse.toModel(): ProgramLanguage =
        ProgramLanguage(
            id = id,
            name = name,
            color = color?.let { Color.parseColor(color) } ?: name.toColorInt(),
            isVerified = isVerified,
        )
}
