package com.voxeldev.canoe.settings

import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.settings.di.settingsFeatureModule
import com.voxeldev.canoe.settings.integration.RevokeAccessTokenUseCase
import com.voxeldev.canoe.utils.BaseUnitTest
import com.voxeldev.canoe.utils.exceptions.TokenNotFoundException
import com.voxeldev.canoe.utils.integration.BaseUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTestRule
import org.koin.test.mock.declareMock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

/**
 * @author nvoxel
 */
class RevokeAccessTokenUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(settingsFeatureModule)
    }

    @Test
    fun `check RevokeAccessTokenUseCase gets data from repository`() = runTest {
        val authenticationRepository = declareMock<AuthenticationRepository>()

        RevokeAccessTokenUseCase().run(params = BaseUseCase.NoParams)

        verify(authenticationRepository).revokeAccessToken()
    }

    @Test
    fun `check RevokeAccessTokenUseCase returns successful response`() = runTest {
        val repositoryResult = Result.success(Unit)

        declareMock<AuthenticationRepository> {
            stub {
                onBlocking { revokeAccessToken() } doReturn repositoryResult
            }
        }

        val useCaseResult = RevokeAccessTokenUseCase().run(params = BaseUseCase.NoParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check RevokeAccessTokenUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<Unit>(
            exception = TokenNotFoundException(),
        )

        declareMock<AuthenticationRepository> {
            stub {
                onBlocking { revokeAccessToken() } doReturn repositoryResult
            }
        }

        val useCaseResult = RevokeAccessTokenUseCase().run(params = BaseUseCase.NoParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
