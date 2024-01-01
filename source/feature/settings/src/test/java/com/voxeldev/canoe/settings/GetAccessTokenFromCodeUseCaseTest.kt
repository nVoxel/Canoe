package com.voxeldev.canoe.settings

import com.voxeldev.canoe.settings.api.AuthenticationRepository
import com.voxeldev.canoe.settings.di.settingsFeatureModule
import com.voxeldev.canoe.settings.integration.GetAccessTokenFromCodeUseCase
import com.voxeldev.canoe.utils.BaseUnitTest
import com.voxeldev.canoe.utils.exceptions.TokenNotFoundException
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTestRule
import org.koin.test.mock.declareMock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

/**
 * @author nvoxel
 */
class GetAccessTokenFromCodeUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(settingsFeatureModule)
    }

    private val defaultRequestParams = "code"

    @Test
    fun `check GetAccessTokenFromCodeUse gets data from repository`() = runTest {
        val authenticationRepository = declareMock<AuthenticationRepository>()

        GetAccessTokenFromCodeUseCase().run(params = defaultRequestParams)

        verify(authenticationRepository).getAccessToken(authorizationCode = defaultRequestParams)
    }

    @Test
    fun `check GetAccessTokenFromCodeUse returns successful response`() = runTest {
        val repositoryResult = Result.success(Unit)

        declareMock<AuthenticationRepository> {
            stub {
                onBlocking { getAccessToken(authorizationCode = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetAccessTokenFromCodeUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetAccessTokenFromCodeUse returns exception`() = runTest {
        val repositoryResult = Result.failure<Unit>(
            exception = TokenNotFoundException(),
        )

        declareMock<AuthenticationRepository> {
            stub {
                onBlocking { getAccessToken(authorizationCode = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetAccessTokenFromCodeUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
