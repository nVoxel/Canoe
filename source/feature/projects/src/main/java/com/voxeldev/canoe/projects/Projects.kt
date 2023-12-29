package com.voxeldev.canoe.projects

import com.arkivanov.decompose.value.Value
import com.voxeldev.canoe.projects.api.ProjectsModel

/**
 * @author nvoxel
 */
interface Projects {

    val model: Value<Model>

    fun onItemClicked(projectId: String)

    fun onSearchTextChanged(text: String)

    fun onToggleSearchClicked()

    fun onSearchClicked()

    data class Model(
        val projectsModel: ProjectsModel,
        val errorText: String?,
        val isLoading: Boolean,
        val searchActive: Boolean,
        val searchText: String,
    )

    sealed class Output {
        data class Selected(val projectId: String) : Output()
    }
}
