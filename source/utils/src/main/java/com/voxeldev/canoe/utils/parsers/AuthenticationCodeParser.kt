package com.voxeldev.canoe.utils.parsers

import android.net.Uri

/**
 * @author nvoxel
 */
fun interface AuthenticationCodeParser {
    fun getAuthenticationCode(uri: Uri): String?
}
