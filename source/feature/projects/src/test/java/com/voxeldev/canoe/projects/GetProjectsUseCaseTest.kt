package com.voxeldev.canoe.projects

import com.voxeldev.canoe.projects.api.ProjectsModel
import com.voxeldev.canoe.projects.api.ProjectsRepository
import com.voxeldev.canoe.projects.api.ProjectsRequest
import com.voxeldev.canoe.projects.di.projectsFeatureModule
import com.voxeldev.canoe.projects.integration.GetProjectsUseCase
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
class GetProjectsUseCaseTest : BaseUnitTest() {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(projectsFeatureModule)
    }

    private val defaultRequestParams = ProjectsRequest()

    @Test
    fun `check GetProjectsUseCase gets data from repository`() = runTest {
        val projectsRepository = declareMock<ProjectsRepository>()

        GetProjectsUseCase().run(params = defaultRequestParams)

        verify(projectsRepository).getProjects(request = defaultRequestParams)
    }

    @Test
    fun `check GetProjectsUseCase returns same value`() = runTest {
        val repositoryResult = Result.success(
            value = ProjectsModel(
                data = listOf(),
            ),
        )

        declareMock<ProjectsRepository> {
            stub {
                onBlocking { getProjects(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetProjectsUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }

    @Test
    fun `check GetProjectsUseCase returns exception`() = runTest {
        val repositoryResult = Result.failure<ProjectsModel>(
            exception = TokenNotFoundException(),
        )

        declareMock<ProjectsRepository> {
            stub {
                onBlocking { getProjects(request = any()) } doReturn repositoryResult
            }
        }

        val useCaseResult = GetProjectsUseCase().run(params = defaultRequestParams)

        assertEquals(
            expected = repositoryResult,
            actual = useCaseResult,
            message = "UseCase should return the same value Repository returns",
        )
    }
}
