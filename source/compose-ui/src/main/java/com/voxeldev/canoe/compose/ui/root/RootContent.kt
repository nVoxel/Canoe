package com.voxeldev.canoe.compose.ui.root

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.FaultyDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.Direction
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.isEnter
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.voxeldev.canoe.compose.ui.dashboard.DashboardContent
import com.voxeldev.canoe.compose.ui.leaderboards.LeaderboardsContent
import com.voxeldev.canoe.compose.ui.projects.ProjectsContent
import com.voxeldev.canoe.compose.ui.settings.SettingsContent
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.root.Root
import com.voxeldev.canoe.root.integration.RootComponent

/**
 * @author nvoxel
 */
@Composable
fun RootContent(component: RootComponent) {
    CanoeTheme {
        Scaffold(
            bottomBar = {
                BottomNavigation(component = component)
            }
        ) { paddingValues ->
            Children(
                component = component,
                modifier = Modifier
                    .padding(
                        top = paddingValues.calculateTopPadding(),
                        bottom = paddingValues.calculateBottomPadding(),
                    ),
            )
        }
    }
}

@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = tabAnimation(),
    ) {
        when (val child = it.instance) {
            is Root.Child.DashboardChild -> DashboardContent(component = child.component)
            is Root.Child.ProjectsChild -> ProjectsContent(component = child.component)
            is Root.Child.SettingsChild -> SettingsContent(component = child.component)
            is Root.Child.LeaderboardsChild -> LeaderboardsContent(component = child.component)
        }
    }
}

@Composable
private fun BottomNavigation(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = activeComponent is Root.Child.DashboardChild,
            onClick = component::onDashboardTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Dashboard",
                )
            },
            label = {
                Text(text = "Dashboard")
            },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            selected = activeComponent is Root.Child.LeaderboardsChild,
            onClick = component::onLeaderboardsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Leaderboard,
                    contentDescription = "Leaderboards",
                )
            },
            label = {
                Text(text = "Leaderboards")
            },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            selected = activeComponent is Root.Child.ProjectsChild,
            onClick = component::onProjectsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Projects",
                )
            },
            label = {
                Text(text = "Projects")
            },
            alwaysShowLabel = false,
        )

        NavigationBarItem(
            selected = activeComponent is Root.Child.SettingsChild,
            onClick = component::onSettingsTabClicked,
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                )
            },
            label = {
                Text(text = "Settings")
            },
            alwaysShowLabel = false,
        )
    }
}

@OptIn(FaultyDecomposeApi::class)
@Composable
private fun tabAnimation(): StackAnimation<Any, Root.Child> =
    stackAnimation { child, otherChild, direction ->
        val index = child.instance.index
        val otherIndex = otherChild.instance.index
        val anim = slide()
        if ((index > otherIndex) == direction.isEnter) anim else anim.flipSide()
    }

private val Root.Child.index: Int
    get() =
        when (this) {
            is Root.Child.DashboardChild -> 0
            is Root.Child.LeaderboardsChild -> 1
            is Root.Child.ProjectsChild -> 2
            is Root.Child.SettingsChild -> 3
        }

private fun StackAnimator.flipSide(): StackAnimator =
    StackAnimator { direction, isInitial, onFinished, content ->
        invoke(
            direction = direction.flipSide(),
            isInitial = isInitial,
            onFinished = onFinished,
            content = content,
        )
    }

private fun Direction.flipSide(): Direction =
    when (this) {
        Direction.ENTER_FRONT -> Direction.ENTER_BACK
        Direction.EXIT_FRONT -> Direction.EXIT_BACK
        Direction.ENTER_BACK -> Direction.ENTER_FRONT
        Direction.EXIT_BACK -> Direction.EXIT_FRONT
    }
