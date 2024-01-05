package com.voxeldev.canoe.utils.extensions

/**
 * @author nvoxel
 */
fun Throwable.getMessage() = message ?: toString()
