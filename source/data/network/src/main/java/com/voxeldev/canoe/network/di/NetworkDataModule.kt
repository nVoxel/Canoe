package com.voxeldev.canoe.network.di

import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRepository
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesRepository
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRepository
import com.voxeldev.canoe.database.di.databaseDataModule
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRepository
import com.voxeldev.canoe.local.di.localDataModule
import com.voxeldev.canoe.network.wakatime.DefaultAllTimeRepository
import com.voxeldev.canoe.network.wakatime.DefaultAuthenticationRepository
import com.voxeldev.canoe.network.wakatime.DefaultLeaderboardsRepository
import com.voxeldev.canoe.network.wakatime.DefaultProgramLanguagesRepository
import com.voxeldev.canoe.network.wakatime.DefaultProjectsRepository
import com.voxeldev.canoe.network.wakatime.DefaultSummariesRepository
import com.voxeldev.canoe.projects.api.ProjectsRepository
import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.settings.api.TokenRepository
import com.voxeldev.canoe.utils.di.utilsModule
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * @author nvoxel
 */
@OptIn(ExperimentalSerializationApi::class)
val networkDataModule = module {

    includes(utilsModule, mappersModule, databaseDataModule, localDataModule)

    single {
        HttpClient(CIO) {
            val stringResourceProvider: StringResourceProvider = get()

            val authenticationRepository: AuthenticationRepository = get()
            val tokenRepository: TokenRepository = get()
            val accessToken: String? = tokenRepository.getAccessToken().getOrNull()
            val refreshToken: String? = tokenRepository.getRefreshToken().getOrNull()

            defaultRequest {
                url(stringResourceProvider.getWakaTimeApiBaseUrl())
            }

            if (accessToken != null && refreshToken != null) {
                install(Auth) {
                    bearer {
                        loadTokens {
                            BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
                        }

                        refreshTokens {
                            authenticationRepository.refreshAccessToken {
                                markAsRefreshTokenRequest()
                            }

                            BearerTokens(
                                accessToken = tokenRepository.getAccessToken().getOrThrow(),
                                refreshToken = tokenRepository.getRefreshToken().getOrThrow(),
                            )
                        }
                    }
                }
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    },
                )
            }
        }
    }

    single(named("oauth")) {
        HttpClient(CIO) {
            val stringResourceProvider: StringResourceProvider = get()

            defaultRequest {
                url(stringResourceProvider.getWakaTimeOAuthBaseUrl())
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }

    single<AuthenticationRepository> {
        DefaultAuthenticationRepository(
            networkHandler = get(),
            httpClient = get(named("oauth")),
            stringResourceProvider = get(),
            tokenRepository = get(),
        )
    }

    single<AllTimeRepository> {
        DefaultAllTimeRepository(
            networkHandler = get(),
            httpClient = get(),
            authenticationRepository = get(),
            allTimeDatabase = get(),
            allTimeMapper = get(),
            allTimeDatabaseMapper = get(),
        )
    }

    single<LeaderboardsRepository> {
        DefaultLeaderboardsRepository(
            networkHandler = get(),
            httpClient = get(),
            authenticationRepository = get(),
            leaderboardsDatabase = get(),
            leaderboardsMapper = get(),
            leaderboardsDatabaseMapper = get(),
        )
    }

    single<ProgramLanguagesRepository> {
        DefaultProgramLanguagesRepository(
            networkHandler = get(),
            httpClient = get(),
            authenticationRepository = get(),
            programLanguagesDatabase = get(),
            programLanguagesMapper = get(),
            programLanguagesDatabaseMapper = get(),
        )
    }

    single<ProjectsRepository> {
        DefaultProjectsRepository(
            networkHandler = get(),
            httpClient = get(),
            authenticationRepository = get(),
            projectsDatabase = get(),
            projectsMapper = get(),
            projectsDatabaseMapper = get(),
        )
    }

    single<SummariesRepository> {
        DefaultSummariesRepository(
            networkHandler = get(),
            httpClient = get(),
            authenticationRepository = get(),
            summariesDatabase = get(),
            summariesMapper = get(),
            summariesDatabaseMapper = get(),
        )
    }
}
