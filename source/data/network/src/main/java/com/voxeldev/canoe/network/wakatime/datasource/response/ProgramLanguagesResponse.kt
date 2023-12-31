package com.voxeldev.canoe.network.wakatime.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author nvoxel
 */
@Serializable
internal data class ProgramLanguagesResponse(
    val data: List<ProgramLanguageResponse>,
    val total: Int,
    @SerialName("total_pages")
    val totalPages: Int,
)

@Serializable
internal data class ProgramLanguageResponse(
    val id: String,
    val name: String,
    val color: String?,
    @SerialName("is_verified")
    val isVerified: Boolean?,
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("modified_at")
    val modifiedAt: String?,
)
