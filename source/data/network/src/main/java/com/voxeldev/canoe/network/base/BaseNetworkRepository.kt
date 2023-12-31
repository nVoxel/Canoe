package com.voxeldev.canoe.network.base

import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.utils.exceptions.NetworkIsNotAvailableException
import com.voxeldev.canoe.utils.exceptions.SubscriptionRequiredException
import com.voxeldev.canoe.utils.exceptions.TokenNotFoundException
import com.voxeldev.canoe.utils.platform.NetworkHandler
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

/**
 * @author nvoxel
 */
internal abstract class BaseNetworkRepository<Model>(
    protected val networkHandler: NetworkHandler,
    protected val httpClient: HttpClient,
    protected val authenticationRepository: AuthenticationRepository,
) {

    protected suspend inline fun <reified Response> doRequest(
        request: HttpRequestBuilder,
        getFromCache: (useOutdatedCache: Boolean) -> Model?,
        cache: Response.() -> Unit,
        transform: Response.() -> Model,
    ): Result<Model> {
        networkHandler.isNetworkAvailable().let { isNetworkAvailable ->
            getFromCache(!isNetworkAvailable)?.let { return Result.success(it) }

            if (!isNetworkAvailable) {
                return Result.failure(NetworkIsNotAvailableException())
            }
        }

        return runCatching {
            val response = httpClient.get(request)

            when (response.status) {
                HttpStatusCode.Unauthorized -> throw TokenNotFoundException()
                HttpStatusCode.PaymentRequired -> throw SubscriptionRequiredException()
            }

            with(response.body<Response>()) {
                cache()
                transform()
            }
        }
    }
}
