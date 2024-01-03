package com.voxeldev.canoe.compose.ui.projects

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Children(
        stack = component.childStack,
        modifier = modifier,
        animation = stackAnimation(slide(orientation = Orientation.Vertical)),
    ) {
        when (val child = it.instance) {
            is Projects.Child.ProjectsListChild -> ProjectsListContent(component = child.component)
            is Projects.Child.ProjectsDetailsChild -> DashboardContent(component = child.component)
        }
    }
}
