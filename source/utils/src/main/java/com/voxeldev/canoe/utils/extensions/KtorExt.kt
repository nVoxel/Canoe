package com.voxeldev.canoe.utils.extensions

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.http.URLBuilder

/**
 * @author nvoxel
 */
fun HttpRequestBuilder.url(urlString: String, block: URLBuilder.() -> Unit) {
    url(urlString)
    url.block()
}
