package com.voxeldev.canoe.dashboard.api.languages

/**
 * @author nvoxel
 */
data class ProgramLanguagesModel(
    val data: List<ProgramLanguage>,
    val total: Int,
    val totalPages: Int,
)

data class ProgramLanguage(
    val id: String,
    val name: String,
    val color: Int,
    val isVerified: Boolean?,
)
