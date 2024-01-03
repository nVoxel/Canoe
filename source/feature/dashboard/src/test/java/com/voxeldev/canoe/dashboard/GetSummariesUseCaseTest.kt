package com.voxeldev.canoe.dashboard

import com.voxeldev.canoe.dashboard.api.sumaries.CumulativeTotal
import com.voxeldev.canoe.dashboard.api.sumaries.DailyAverage
import com.voxeldev.canoe.dashboard.api.sumaries.DailyChartData
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRepository
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRequest
import com.voxeldev.canoe.dashboard.di.dashboardFeatureModule
import com.voxeldev.canoe.dashboard.integration.GetSummariesUseCase
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
class GetSummariesUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(dashboardFeatureModule)
    }

    private val defaultRequestParams = SummariesRequest(startDate = "yyyy-MM-dd", endDate = "yyyy-MM-dd")

    @Test
    fun `check GetSummariesUseCase gets data from repository`() = runTest {
        val summariesRepository = declareMock<SummariesRepository>()

        GetSummariesUseCase().run(params = defaultRequestParams)

        verify(summariesRepository).getSummaries(request = defaultRequestParams)
    }

    @Test
    fun `check GetSummariesUseCase returns same value`() = runTest {
        val repositoryResult = Result.success(
            value = SummariesModel(
                dailyChartData = DailyChartData(
                    projectsSeries = hashMapOf(),
                    totalLabels = mutableListOf(),
                    horizontalLabels = mutableListOf(),
                ),
                languagesChartData = listOf(),
                editorsChartData = listOf(),
                operatingSystemsChartData = listOf(),
                machinesChartData = listOf(),
                cumulativeTotal = CumulativeTotal(seconds = 0f, text = "", digital = ""),
                dailyAverage = DailyAverage(seconds = 0f, text = ""),
            ),
        )

        declareMock<SummariesRepository> {
            stub {
                onBlocking { getSummaries(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetSummariesUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetSummariesUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<SummariesModel>(
            exception = TokenNotFoundException(),
        )

        declareMock<SummariesRepository> {
            stub {
                onBlocking { getSummaries(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetSummariesUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
