package com.voxeldev.canoe.projects.api

/**
 * @author nvoxel
 */
data class ProjectsModel(
    val data: List<Project>,
    val message: String? = null,
)

data class Project(
    val id: String,
    val name: String,
    val repository: String?,
    val badge: String?,
    val color: String?,
    val hasPublicUrl: Boolean,
    val humanReadableLastHeartBeatAt: String?,
    val lastHeartBeatAt: String?,
    val url: String,
    val urlEncodedName: String,
    val createdAt: String,
)
