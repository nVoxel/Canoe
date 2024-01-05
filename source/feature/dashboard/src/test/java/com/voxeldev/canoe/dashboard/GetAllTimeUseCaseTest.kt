package com.voxeldev.canoe.dashboard

import com.voxeldev.canoe.dashboard.api.alltime.AllTimeData
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRepository
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRequest
import com.voxeldev.canoe.dashboard.di.dashboardFeatureModule
import com.voxeldev.canoe.dashboard.integration.GetAllTimeUseCase
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
class GetAllTimeUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(dashboardFeatureModule)
    }

    private val defaultRequestParams = AllTimeRequest(project = "project")

    @Test
    fun `check GetAllTimeUseCase gets data from repository`() = runTest {
        val allTimeRepository = declareMock<AllTimeRepository>()

        GetAllTimeUseCase().run(params = defaultRequestParams)

        verify(allTimeRepository).getAllTime(request = defaultRequestParams)
    }

    @Test
    fun `check GetAllTimeUseCase returns same value`() = runTest {
        val repositoryResult = Result.success(
            value = AllTimeModel(
                data = AllTimeData(
                    decimal = "",
                    digital = "",
                    text = "",
                    totalSeconds = 0f,
                ),
                message = "",
            ),
        )

        declareMock<AllTimeRepository> {
            stub {
                onBlocking { getAllTime(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetAllTimeUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetAllTimeUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<AllTimeModel>(
            exception = TokenNotFoundException(),
        )

        declareMock<AllTimeRepository> {
            stub {
                onBlocking { getAllTime(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetAllTimeUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
