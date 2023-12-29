package com.voxeldev.canoe.root.integration

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.dashboard.integration.DashboardComponent
import com.voxeldev.canoe.leaderboards.Leaderboards
import com.voxeldev.canoe.leaderboards.integration.LeaderboardsComponent
import com.voxeldev.canoe.projects.Projects
import com.voxeldev.canoe.projects.integration.ProjectsComponent
import com.voxeldev.canoe.root.Root
import com.voxeldev.canoe.settings.Settings
import com.voxeldev.canoe.settings.api.TokenRepository
import com.voxeldev.canoe.settings.integration.SettingsComponent
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent

/**
 * @author nvoxel
 */
class RootComponent internal constructor(
    componentContext: ComponentContext,
    private val linkHandler: LinkHandler,
    private val tokenRepository: TokenRepository,
    private val dashboardComponent: (ComponentContext) -> DashboardComponent,
    private val leaderboardsComponent: (ComponentContext, (Leaderboards.Output) -> Unit) -> LeaderboardsComponent,
    private val projectsComponent: (ComponentContext, (Projects.Output) -> Unit) -> ProjectsComponent,
    private val settingsComponent: (ComponentContext, (Settings.Output) -> Unit) -> SettingsComponent,
) : Root, KoinComponent, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
        linkHandler: LinkHandler,
        tokenRepository: TokenRepository,
        stringResourceProvider: StringResourceProvider,
        deepLink: Uri?,
    ) : this(
        componentContext = componentContext,
        linkHandler = linkHandler,
        tokenRepository = tokenRepository,
        dashboardComponent = { childContext ->
            DashboardComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
            )
        },
        leaderboardsComponent = { childContext, output ->
            LeaderboardsComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output,
            )
        },
        projectsComponent = { childContext, output ->
            ProjectsComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                output = output,
            )
        },
        settingsComponent = { childContext, output ->
            SettingsComponent(
                componentContext = childContext,
                storeFactory = storeFactory,
                stringResourceProvider = stringResourceProvider,
                deepLink = deepLink,
                output = output,
            )
        },
    )

    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, Root.Child>> =
        childStack(
            source = navigation,
            serializer = serializer(),
            initialConfiguration = tokenRepository.getAccessToken().fold(
                onSuccess = { Config.Dashboard },
                onFailure = { Config.Settings },
            ),
            handleBackButton = true,
            childFactory = ::createChild
        )

    private fun createChild(config: Config, componentContext: ComponentContext): Root.Child =
        when (config) {
            is Config.Dashboard -> Root.Child.DashboardChild(
                component = dashboardComponent(componentContext)
            )
            is Config.Leaderboards -> Root.Child.LeaderboardsChild(
                component = leaderboardsComponent(componentContext) { output -> onLeaderboardsOutput(output = output) }
            )
            is Config.Projects -> Root.Child.ProjectsChild(
                component = projectsComponent(componentContext) { output -> onProjectsOutput(output = output) }
            )
            is Config.Settings -> Root.Child.SettingsChild(
                component = settingsComponent(componentContext) { output -> onSettingsOutput(output = output) }
            )
        }

    private fun onLeaderboardsOutput(output: Leaderboards.Output) {
        when (output) {
            is Leaderboards.Output.Selected -> linkHandler.openLink(url = output.profileUrl)
        }
    }

    private fun onProjectsOutput(output: Projects.Output) {
        when (output) {
            is Projects.Output.Selected -> println(message = output.projectId)
        }
    }

    private fun onSettingsOutput(output: Settings.Output) {
        when (output) {
            is Settings.Output.Connect -> linkHandler.openLink(url = output.connectUrl)
        }
    }

    override fun onDashboardTabClicked() {
        navigation.bringToFront(Config.Dashboard)
    }

    override fun onProjectsTabClicked() {
        navigation.bringToFront(Config.Projects)
    }

    override fun onSettingsTabClicked() {
        navigation.bringToFront(Config.Settings)
    }

    override fun onLeaderboardsTabClicked() {
        navigation.bringToFront(Config.Leaderboards)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Dashboard : Config

        @Serializable
        data object Projects : Config

        @Serializable
        data object Settings : Config

        @Serializable
        data object Leaderboards : Config
    }
}
