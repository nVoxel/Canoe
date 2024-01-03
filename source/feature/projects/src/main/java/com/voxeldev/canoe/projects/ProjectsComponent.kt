package com.voxeldev.canoe.projects

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.dashboard.integration.DashboardComponent
import com.voxeldev.canoe.projects.list.ProjectsList
import com.voxeldev.canoe.projects.list.ProjectsListComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer

/**
 * @author nvoxel
 */
class ProjectsComponent internal constructor(
    componentContext: ComponentContext,
    private val projectsListComponent: (ComponentContext, (ProjectsList.Output) -> Unit) -> ProjectsListComponent,
    private val projectsDetailsComponent: (ComponentContext, String) -> DashboardComponent,
) : Projects, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
    ) : this(
        componentContext = componentContext,
        projectsListComponent = { childContext, output ->
            ProjectsListComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output,
            )
        },
        projectsDetailsComponent = { childContext, projectName ->
            DashboardComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                projectName = projectName,
            )
        },
    )

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Projects.Child>> =
        childStack(
            source = navigation,
            serializer = serializer(),
            initialConfiguration = Config.ProjectsList,
            handleBackButton = true,
            childFactory = ::createChild,
        )

    private fun createChild(config: Config, componentContext: ComponentContext): Projects.Child =
        when (config) {
            is Config.ProjectsList -> Projects.Child.ProjectsListChild(
                component = projectsListComponent(componentContext) { output -> onProjectsListOutput(output = output) },
            )

            is Config.ProjectsDetails -> Projects.Child.ProjectsDetailsChild(
                component = projectsDetailsComponent(componentContext, config.projectName),
            )
        }

    private fun onProjectsListOutput(output: ProjectsList.Output) =
        when (output) {
            is ProjectsList.Output.Selected -> navigation.push(Config.ProjectsDetails(projectName = output.projectName))
        }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object ProjectsList : Config

        @Serializable
        data class ProjectsDetails(val projectName: String) : Config
    }
}
