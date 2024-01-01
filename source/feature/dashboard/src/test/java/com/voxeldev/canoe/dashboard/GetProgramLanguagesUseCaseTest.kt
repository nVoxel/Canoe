package com.voxeldev.canoe.dashboard

import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguage
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesRepository
import com.voxeldev.canoe.dashboard.di.dashboardFeatureModule
import com.voxeldev.canoe.dashboard.integration.GetProgramLanguagesUseCase
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
class GetProgramLanguagesUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(dashboardFeatureModule)
    }

    @Test
    fun `check GetProgramLanguagesUseCase gets data from repository`() = runTest {
        val programLanguagesRepository = declareMock<ProgramLanguagesRepository>()

        GetProgramLanguagesUseCase().run(params = BaseUseCase.NoParams)

        verify(programLanguagesRepository).getProgramLanguages()
    }

    @Test
    fun `check GetProgramLanguagesUseCase returns same value`() = runTest {
        val repositoryResult = Result.success(
            value = ProgramLanguagesModel(
                data = listOf(ProgramLanguage(id = "1", name = "Kotlin", color = 0x00FF00, isVerified = true)),
                total = 1,
                totalPages = 1,
            )
        )

        declareMock<ProgramLanguagesRepository> {
            stub {
                onBlocking { getProgramLanguages() } doReturn repositoryResult
            }
        }

        val useCaseResult = GetProgramLanguagesUseCase().run(params = BaseUseCase.NoParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetProgramLanguagesUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<ProgramLanguagesModel>(
            exception = TokenNotFoundException(),
        )

        declareMock<ProgramLanguagesRepository> {
            stub {
                onBlocking { getProgramLanguages() } doReturn repositoryResult
            }
        }

        val useCaseResult = GetProgramLanguagesUseCase().run(params = BaseUseCase.NoParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
