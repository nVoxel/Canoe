package com.voxeldev.canoe.compose.ui.projects

import android.content.res.Configuration
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.voxeldev.canoe.compose.ui.dashboard.DashboardContent
import com.voxeldev.canoe.projects.Projects
import com.voxeldev.canoe.projects.ProjectsComponent

/**
 * @author nvoxel
 */
@Composable
internal fun ProjectsContent(component: ProjectsComponent) {
    Children(
        component = component,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
private fun Children(component: ProjectsComponent, modifier: Modifier = Modifier) {
    val orientation = LocalConfiguration.current.orientation

    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(
            slide(
                orientation = if (orientation == Configuration.ORIENTATION_PORTRAIT) Orientation.Vertical else Orientation.Horizontal,
            ),
        ),
    ) {
        when (val child = it.instance) {
            is Projects.Child.ProjectsListChild -> ProjectsListContent(component = child.component)
            is Projects.Child.ProjectsDetailsChild -> DashboardContent(component = child.component)
        }
    }
}
