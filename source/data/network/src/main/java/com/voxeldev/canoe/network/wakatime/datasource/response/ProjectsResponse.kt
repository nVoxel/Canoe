package com.voxeldev.canoe.network.wakatime.datasource.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author nvoxel
 */
@Serializable
internal data class ProjectsResponse(
    val data: List<ProjectResponse>,
    val message: String? = null,
)

@Serializable
internal data class ProjectResponse(
    val id: String,
    val name: String,
    val repository: String?,
    val badge: String?,
    val color: String?,
    @SerialName("has_public_url")
    val hasPublicUrl: Boolean,
    @SerialName("human_readable_last_heartbeat_at")
    val humanReadableLastHeartBeatAt: String?,
    @SerialName("last_heartbeat_at")
    val lastHeartBeatAt: String?,
    val url: String,
    @SerialName("urlencoded_name")
    val urlEncodedName: String,
    @SerialName("created_at")
    val createdAt: String,
)
