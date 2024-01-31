package com.voxeldev.canoe.leaderboards

import com.voxeldev.canoe.leaderboards.api.CurrentUser
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRepository
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import com.voxeldev.canoe.leaderboards.api.TimeRange
import com.voxeldev.canoe.leaderboards.api.User
import com.voxeldev.canoe.leaderboards.di.leaderboardsFeatureModule
import com.voxeldev.canoe.leaderboards.integration.GetLeaderboardsAsyncUseCase
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
class GetLeaderboardsAsyncUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(leaderboardsFeatureModule)
    }

    private val defaultRequestParams = LeaderboardsRequest()

    @Test
    fun `check GetLeaderboardsUseCase gets data from repository`() = runTest {
        val leaderboardsRepository = declareMock<LeaderboardsRepository>()

        GetLeaderboardsAsyncUseCase().run(params = defaultRequestParams)

        verify(leaderboardsRepository).getLeaderboards(request = defaultRequestParams)
    }

    @Test
    fun `check GetLeaderboardsUseCase returns same value`() = runTest {
        val repositoryResult = Result.success(
            value = LeaderboardsModel(
                currentUser = CurrentUser(user = User(id = "1")),
                data = listOf(),
                page = 1,
                totalPages = 1,
                range = TimeRange(),
                modifiedAt = "0",
                timeout = 0,
                writesOnly = false,
            ),
        )

        declareMock<LeaderboardsRepository> {
            stub {
                onBlocking { getLeaderboards(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetLeaderboardsAsyncUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetLeaderboardsUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<LeaderboardsModel>(
            exception = TokenNotFoundException(),
        )

        declareMock<LeaderboardsRepository> {
            stub {
                onBlocking { getLeaderboards(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetLeaderboardsAsyncUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
