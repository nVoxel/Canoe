package com.voxeldev.canoe.utils.extensions

/**
 * @author nvoxel
 */
fun String.toColorInt(): Int {
    val hashCode = hashCode()

    val red = (hashCode and RED_MASK) shr RED_SHIFT
    val green = (hashCode and GREEN_MASK) shr GREEN_SHIFT
    val blue = hashCode and BLUE_MASK

    return (ALPHA_VALUE shl ALPHA_SHIFT) or (red shl RED_SHIFT) or (green shl GREEN_SHIFT) or blue
}

private const val RED_MASK = 0xFF0000
private const val GREEN_MASK = 0x00FF00
private const val BLUE_MASK = 0x0000FF

private const val ALPHA_VALUE = 255

private const val ALPHA_SHIFT = 24
private const val RED_SHIFT = 16
private const val GREEN_SHIFT = 8
