package com.voxeldev.canoe.projects.list

import com.arkivanov.decompose.value.Value
import com.voxeldev.canoe.projects.api.ProjectsModel

/**
 * @author nvoxel
 */
interface ProjectsList {

    val model: Value<Model>

    fun onItemClicked(projectName: String)

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
        data class Selected(val projectName: String) : Output()
    }
}
