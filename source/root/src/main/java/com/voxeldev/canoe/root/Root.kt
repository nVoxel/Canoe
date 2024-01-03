package com.voxeldev.canoe.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.voxeldev.canoe.dashboard.integration.DashboardComponent
import com.voxeldev.canoe.leaderboards.integration.LeaderboardsComponent
import com.voxeldev.canoe.projects.ProjectsComponent
import com.voxeldev.canoe.settings.integration.SettingsComponent

/**
 * @author nvoxel
 */
interface Root {

    val childStack: Value<ChildStack<*, Child>>

    fun onDashboardTabClicked()
    fun onProjectsTabClicked()
    fun onSettingsTabClicked()
    fun onLeaderboardsTabClicked()

    sealed class Child {
        class DashboardChild(val component: DashboardComponent) : Child()
        class ProjectsChild(val component: ProjectsComponent) : Child()
        class SettingsChild(val component: SettingsComponent) : Child()
        class LeaderboardsChild(val component: LeaderboardsComponent) : Child()
    }
}
