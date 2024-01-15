package com.voxeldev.canoe.compose.ui.root

import android.content.res.Configuration
import androidx.activity.ComponentActivity
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.CustomNavigationRail
import com.voxeldev.canoe.compose.ui.components.decompose.slideTabAnimation
import com.voxeldev.canoe.compose.ui.dashboard.DashboardContent
import com.voxeldev.canoe.compose.ui.leaderboards.LeaderboardsContent
import com.voxeldev.canoe.compose.ui.projects.ProjectsContent
import com.voxeldev.canoe.compose.ui.settings.SettingsContent
import com.voxeldev.canoe.compose.ui.theme.AdditionalIcons
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.compose.ui.theme.SystemBarStyle
import com.voxeldev.canoe.compose.ui.theme.enableEdgeToEdge
import com.voxeldev.canoe.root.Root
import com.voxeldev.canoe.root.integration.RootComponent

private val navigationTonalElevation = 3.0.dp

/**
 * @author nvoxel
 */
@Composable
fun RootContent(component: RootComponent) {
    CanoeTheme {
        val navigationBarScrim = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation = navigationTonalElevation).toArgb()

        (LocalContext.current as ComponentActivity).enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle(
                lightScrim = navigationBarScrim,
                darkScrim = navigationBarScrim,
            )
        )

        LocalConfiguration.current.orientation.let { orientation ->
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                VerticalLayout(component = component)
            } else {
                HorizontalLayout(component = component)
            }
        }
    }
}

@Composable
private fun VerticalLayout(component: RootComponent) {
    Scaffold(
        bottomBar = {
            BottomNavigation(component = component)
        },
    ) { paddingValues ->
        Children(
            component = component,
            modifier = Modifier.padding(
                bottom = paddingValues.calculateBottomPadding(),
            ),
            isHorizontalLayout = false,
        )
    }
}

@Composable
private fun HorizontalLayout(component: RootComponent) {
    val layoutDirection = LocalLayoutDirection.current

    Scaffold { paddingValues ->
        Row(
            modifier = Modifier
                .padding(
                    start = paddingValues.calculateStartPadding(layoutDirection = layoutDirection),
                    end = paddingValues.calculateEndPadding(layoutDirection = layoutDirection),
                    bottom = paddingValues.calculateBottomPadding(),
                )
        ) {
            SideNavigation(component = component)
            Children(
                component = component,
                isHorizontalLayout = true,
            )
        }
    }
}

@Composable
private fun Children(component: RootComponent, modifier: Modifier = Modifier, isHorizontalLayout: Boolean) {
    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = slideTabAnimation(
            orientation = if (isHorizontalLayout) Orientation.Vertical else Orientation.Horizontal
        ) { index() },
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

    NavigationBar(
        modifier = modifier,
        tonalElevation = navigationTonalElevation,
    ) {
        NavigationBarItem(
            selected = activeComponent is Root.Child.DashboardChild,
            onClick = component::onDashboardTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Dashboard,
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
                    imageVector = AdditionalIcons.Leaderboard,
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

@Composable
private fun SideNavigation(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    CustomNavigationRail(
        modifier = modifier
            .zIndex(1f),
        containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(elevation = navigationTonalElevation),
    ) {
        NavigationRailItem(
            selected = activeComponent is Root.Child.DashboardChild,
            onClick = component::onDashboardTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Dashboard,
                    contentDescription = "Dashboard",
                )
            },
            label = {
                Text(text = "Dashboard")
            },
            alwaysShowLabel = false,
        )

        NavigationRailItem(
            selected = activeComponent is Root.Child.LeaderboardsChild,
            onClick = component::onLeaderboardsTabClicked,
            icon = {
                Icon(
                    imageVector = AdditionalIcons.Leaderboard,
                    contentDescription = "Leaderboards",
                )
            },
            label = {
                Text(text = "Leaderboards")
            },
            alwaysShowLabel = false,
        )

        NavigationRailItem(
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

        NavigationRailItem(
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

private fun Root.Child.index(): Int =
    when (this) {
        is Root.Child.DashboardChild -> 0
        is Root.Child.LeaderboardsChild -> 1
        is Root.Child.ProjectsChild -> 2
        is Root.Child.SettingsChild -> 3
    }
