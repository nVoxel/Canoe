package com.voxeldev.canoe.utils.parsers

import android.net.Uri

/**
 * @author nvoxel
 */
internal class DefaultAuthenticationCodeParser : AuthenticationCodeParser {

    override fun getAuthenticationCode(uri: Uri): String? {
        return uri.encodedQuery?.split('=')?.getOrNull(1)
    }
}
