package com.voxeldev.canoe.projects.list

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.perf.performance
import com.voxeldev.canoe.projects.api.ProjectsModel
import com.voxeldev.canoe.projects.api.ProjectsRequest
import com.voxeldev.canoe.projects.integration.GetProjectsUseCase
import com.voxeldev.canoe.projects.list.ProjectsListStore.Intent
import com.voxeldev.canoe.projects.list.ProjectsListStore.State
import com.voxeldev.canoe.utils.analytics.CustomEvent
import com.voxeldev.canoe.utils.analytics.CustomTrace
import com.voxeldev.canoe.utils.analytics.logEvent
import com.voxeldev.canoe.utils.analytics.startTrace

/**
 * @author nvoxel
 */
internal class ProjectsListStoreProvider(
    private val storeFactory: StoreFactory,
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics,
    private val getProjectsUseCase: GetProjectsUseCase = GetProjectsUseCase(),
) {

    fun provide(): ProjectsListStore =
        object :
            ProjectsListStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class ProjectsListLoaded(val projectsModel: ProjectsModel) : Msg()
        data object ProjectsListLoading : Msg()
        data class Error(val message: String) : Msg()
        data class TextChanged(val text: String) : Msg()
        data class SearchToggled(val active: Boolean) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) =
            loadProjects(params = ProjectsRequest(searchQuery = getState().searchText))

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetSearchText -> dispatch(message = Msg.TextChanged(text = intent.text))
                is Intent.ToggleSearch -> dispatch(message = Msg.SearchToggled(active = !getState().searchActive))
                is Intent.Search -> loadProjects(params = ProjectsRequest(searchQuery = getState().searchText))
            }
        }

        private fun loadProjects(params: ProjectsRequest) {
            val trace = Firebase.performance.startTrace(trace = CustomTrace.ProjectsLoadTrace)
            dispatch(message = Msg.ProjectsListLoading)
            getProjectsUseCase(params = params, scope = scope) { result ->
                result
                    .fold(
                        onSuccess = {
                            firebaseAnalytics.logEvent(event = CustomEvent.LoadedProjects)
                            dispatch(message = Msg.ProjectsListLoaded(projectsModel = it))
                        },
                        onFailure = { dispatch(message = Msg.Error(message = it.message ?: it.toString())) },
                    )
                    .also { trace.stop() }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.ProjectsListLoaded -> copy(projectsModel = msg.projectsModel, isLoading = false)
                is Msg.ProjectsListLoading -> copy(isLoading = true, errorText = null)
                is Msg.Error -> copy(errorText = msg.message, isLoading = false)
                is Msg.TextChanged -> copy(searchText = msg.text)
                is Msg.SearchToggled -> copy(searchActive = msg.active)
            }
    }

    private companion object {
        const val STORE_NAME = "ProjectsListStore"
    }
}
