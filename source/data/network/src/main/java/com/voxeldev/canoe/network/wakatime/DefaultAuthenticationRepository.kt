package com.voxeldev.canoe.network.wakatime

import com.voxeldev.canoe.network.wakatime.datasource.response.AuthenticationResponse
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.settings.api.TokenRepository
import com.voxeldev.canoe.utils.exceptions.NetworkIsNotAvailableException
import com.voxeldev.canoe.utils.exceptions.TokenNotFoundException
import com.voxeldev.canoe.utils.platform.NetworkHandler
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.parameters

/**
 * @author nvoxel
 */
internal class DefaultAuthenticationRepository(
    private val networkHandler: NetworkHandler,
    private val httpClient: HttpClient,
    private val stringResourceProvider: StringResourceProvider,
    private val tokenRepository: TokenRepository,
) : AuthenticationRepository {

    override suspend fun getAccessToken(authorizationCode: String): Result<Unit> =
        doAuthRequest<AuthenticationResponse>(
            url = OAUTH_TOKEN_URL,
            formParameters = parameters {
                append(CLIENT_ID_PARAM, stringResourceProvider.getOAuthClientId())
                append(CLIENT_SECRET_PARAM, stringResourceProvider.getOAuthClientSecret())
                append(REDIRECT_URI_PARAM, stringResourceProvider.getOAuthRedirectUrl())
                append(GRANT_TYPE_PARAM, GRANT_TYPE_CODE)
                append(CODE_PARAM, authorizationCode)
            },
        ).map { response ->
            tokenRepository.setAccessToken(response.accessToken)
            tokenRepository.setRefreshToken(response.refreshToken)
        }

    override suspend fun refreshAccessToken(block: HttpRequestBuilder.() -> Unit): Result<Unit> =
        doAuthRequest<AuthenticationResponse>(
            url = OAUTH_TOKEN_URL,
            formParameters = parameters {
                append(CLIENT_ID_PARAM, stringResourceProvider.getOAuthClientId())
                append(CLIENT_SECRET_PARAM, stringResourceProvider.getOAuthClientSecret())
                append(REDIRECT_URI_PARAM, stringResourceProvider.getOAuthRedirectUrl())
                append(GRANT_TYPE_PARAM, GRANT_TYPE_TOKEN)
                append(REFRESH_TOKEN_PARAM, tokenRepository.getRefreshToken().getOrThrow())
            },
            block = block,
        ).map { response ->
            tokenRepository.setAccessToken(response.accessToken)
            tokenRepository.setRefreshToken(response.refreshToken)
        }

    override suspend fun revokeAccessToken(): Result<Unit> =
        doAuthRequest<Unit>(
            url = REVOKE_TOKEN_URL,
            formParameters = parameters {
                append(CLIENT_ID_PARAM, stringResourceProvider.getOAuthClientId())
                append(CLIENT_SECRET_PARAM, stringResourceProvider.getOAuthClientSecret())
                // append(ALL_PARAM, ALL_TRUE)
                append(TOKEN_PARAM, tokenRepository.getAccessToken().getOrThrow())
            },
        ).map {
            tokenRepository.clearAccessToken()
            tokenRepository.clearRefreshToken()
        }

    private suspend inline fun <reified T> doAuthRequest(
        url: String,
        formParameters: Parameters,
        noinline block: HttpRequestBuilder.() -> Unit = {},
    ): Result<T> {
        if (!networkHandler.isNetworkAvailable()) {
            return Result.failure(NetworkIsNotAvailableException())
        }

        return runCatching {
            val response = httpClient.submitForm(url = url, formParameters = formParameters, block = block)

            if (response.status == HttpStatusCode.Unauthorized) throw TokenNotFoundException()

            response.body()
        }
    }

    private companion object {
        const val OAUTH_TOKEN_URL = "token"
        const val REVOKE_TOKEN_URL = "revoke"
        const val CLIENT_ID_PARAM = "client_id"
        const val CLIENT_SECRET_PARAM = "client_secret"
        const val ALL_PARAM = "all"
        const val ALL_TRUE = "true"
        const val REDIRECT_URI_PARAM = "redirect_uri"
        const val GRANT_TYPE_PARAM = "grant_type"
        const val GRANT_TYPE_CODE = "authorization_code"
        const val GRANT_TYPE_TOKEN = "refresh_token"
        const val CODE_PARAM = "code"
        const val TOKEN_PARAM = "token"
        const val REFRESH_TOKEN_PARAM = "refresh_token"
    }
}
