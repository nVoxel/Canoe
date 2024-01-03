package com.voxeldev.canoe.projects

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.voxeldev.canoe.dashboard.integration.DashboardComponent
import com.voxeldev.canoe.projects.list.ProjectsListComponent

/**
 * @author nvoxel
 */
interface Projects {

    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class ProjectsListChild(val component: ProjectsListComponent) : Child()
        class ProjectsDetailsChild(val component: DashboardComponent) : Child()
    }
}
