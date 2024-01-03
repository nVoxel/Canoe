package com.voxeldev.canoe.settings

import com.voxeldev.canoe.settings.api.TokenRepository
import com.voxeldev.canoe.settings.di.settingsFeatureModule
import com.voxeldev.canoe.settings.integration.GetAccessTokenFromStorageUseCase
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
class GetAccessTokenFromStorageUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(settingsFeatureModule)
    }

    @Test
    fun `check GetAccessTokenFromStorageUseCase gets data from repository`() = runTest {
        val tokenRepository = declareMock<TokenRepository>()

        GetAccessTokenFromStorageUseCase().run(params = BaseUseCase.NoParams)

        verify(tokenRepository).getAccessToken()
    }

    @Test
    fun `check GetAccessTokenFromStorageUseCase returns same value`() = runTest {
        val repositoryResult = Result.success(
            value = "token",
        )

        declareMock<TokenRepository> {
            stub {
                onBlocking { getAccessToken() } doReturn repositoryResult
            }
        }

        val useCaseResult = GetAccessTokenFromStorageUseCase().run(params = BaseUseCase.NoParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetAccessTokenFromStorageUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<String>(
            exception = TokenNotFoundException(),
        )

        declareMock<TokenRepository> {
            stub {
                onBlocking { getAccessToken() } doReturn repositoryResult
            }
        }

        val useCaseResult = GetAccessTokenFromStorageUseCase().run(params = BaseUseCase.NoParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
