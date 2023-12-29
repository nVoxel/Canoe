package com.voxeldev.canoe.projects.integration

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.voxeldev.canoe.projects.Projects
import com.voxeldev.canoe.projects.Projects.Output
import com.voxeldev.canoe.projects.store.ProjectsStore
import com.voxeldev.canoe.projects.store.ProjectsStoreProvider
import com.voxeldev.canoe.utils.extensions.asValue

/**
 * @author nvoxel
 */
class ProjectsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: (Output) -> Unit,
) : Projects, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        ProjectsStoreProvider(storeFactory = storeFactory).provide()
    }

    override val model: Value<Projects.Model> = store.asValue().map { state ->
        Projects.Model(
            projectsModel = state.projectsModel,
            searchText = state.searchText,
            errorText = state.errorText,
            searchActive = state.searchActive,
            isLoading = state.isLoading,
        )
    }

    override fun onItemClicked(projectId: String) = output(Output.Selected(projectId = projectId))

    override fun onSearchTextChanged(text: String) = store.accept(
        intent = ProjectsStore.Intent.SetSearchText(text = text)
    )

    override fun onToggleSearchClicked() = store.accept(intent = ProjectsStore.Intent.ToggleSearch)

    override fun onSearchClicked() = store.accept(intent = ProjectsStore.Intent.Search)
}
