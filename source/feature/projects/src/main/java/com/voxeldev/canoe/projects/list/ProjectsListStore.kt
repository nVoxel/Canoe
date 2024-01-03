package com.voxeldev.canoe.projects.list

import com.arkivanov.mvikotlin.core.store.Store
import com.voxeldev.canoe.projects.api.ProjectsModel
import com.voxeldev.canoe.projects.list.ProjectsListStore.Intent
import com.voxeldev.canoe.projects.list.ProjectsListStore.State

/**
 * @author nvoxel
 */
internal interface ProjectsListStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data class SetSearchText(val text: String) : Intent()
        data object ToggleSearch : Intent()
        data object Search : Intent()
    }

    data class State(
        val projectsModel: ProjectsModel = ProjectsModel(data = emptyList()),
        val searchText: String = "",
        val errorText: String? = null,
        val searchActive: Boolean = false,
        val isLoading: Boolean = true,
    )
}
