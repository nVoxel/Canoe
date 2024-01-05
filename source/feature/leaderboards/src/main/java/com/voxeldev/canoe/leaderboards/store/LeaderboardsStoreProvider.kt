package com.voxeldev.canoe.leaderboards.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.perf.performance
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import com.voxeldev.canoe.leaderboards.integration.GetLeaderboardsUseCase
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.Intent
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.State
import com.voxeldev.canoe.utils.analytics.CustomEvent
import com.voxeldev.canoe.utils.analytics.CustomTrace
import com.voxeldev.canoe.utils.analytics.logEvent
import com.voxeldev.canoe.utils.analytics.startTrace
import com.voxeldev.canoe.utils.extensions.getMessage

/**
 * @author nvoxel
 */
internal class LeaderboardsStoreProvider(
    private val storeFactory: StoreFactory,
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics,
    private val getLeaderboardsUseCase: GetLeaderboardsUseCase = GetLeaderboardsUseCase(),
) {

    fun provide(): LeaderboardsStore =
        object :
            LeaderboardsStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class LeaderboardsLoaded(val leaderboardsModel: LeaderboardsModel) : Msg()
        data object LeaderboardsLoading : Msg()
        data class Error(val message: String) : Msg()
        data class LanguageChanged(val language: String?) : Msg()
        data class HireableChanged(val hireable: Boolean?) : Msg()
        data class CountryCodeChanged(val countryCode: String?) : Msg()
        data class FilterBottomSheetToggled(val active: Boolean) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {
        private var lastFilterStateHash: Int? = null

        override fun executeAction(action: Unit, getState: () -> State) =
            loadLeaderboards(
                params = getLeaderboardsRequest(getState().filterBottomSheetState),
            )

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.SetLanguage -> dispatch(message = Msg.LanguageChanged(language = intent.language))
                is Intent.SetHireable -> dispatch(message = Msg.HireableChanged(hireable = intent.hireable))
                is Intent.SetCountryCode -> dispatch(message = Msg.CountryCodeChanged(countryCode = intent.countryCode))
                is Intent.ReloadLeaderboards -> loadLeaderboards(
                    params = getLeaderboardsRequest(getState().filterBottomSheetState),
                )

                is Intent.ResetFilters -> {
                    dispatch(message = Msg.LanguageChanged(language = null))
                    dispatch(message = Msg.HireableChanged(hireable = null))
                    dispatch(message = Msg.CountryCodeChanged(countryCode = null))
                }

                is Intent.ToggleFilterBottomSheet -> {
                    val state = getState()
                    val isActive = state.filterBottomSheetState.active

                    // reload list only when necessary
                    if (isActive && state.filterBottomSheetState.hashCode() != lastFilterStateHash) {
                        loadLeaderboards(params = getLeaderboardsRequest(state = state.filterBottomSheetState))
                    }

                    lastFilterStateHash = state.filterBottomSheetState.copy(active = !isActive).hashCode()

                    dispatch(message = Msg.FilterBottomSheetToggled(active = !isActive))
                }
            }
        }

        private fun loadLeaderboards(params: LeaderboardsRequest) {
            val trace = Firebase.performance.startTrace(trace = CustomTrace.LeaderboardsLoadTrace)
            dispatch(message = Msg.LeaderboardsLoading)
            getLeaderboardsUseCase(params = params, scope = scope) { result ->
                result
                    .fold(
                        onSuccess = {
                            firebaseAnalytics.logEvent(event = CustomEvent.LoadedLeaderboards)
                            dispatch(message = Msg.LeaderboardsLoaded(leaderboardsModel = it))
                        },
                        onFailure = { dispatch(message = Msg.Error(message = it.getMessage())) },
                    )
                    .also { trace.stop() }
            }
        }

        private fun getLeaderboardsRequest(state: LeaderboardsStore.FilterBottomSheetState): LeaderboardsRequest =
            LeaderboardsRequest(
                language = state.selectedLanguage,
                isHireable = state.hireable,
                countryCode = state.selectedCountryCode,
            )
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.LeaderboardsLoaded -> copy(
                    leaderboardsModel = msg.leaderboardsModel,
                    errorText = null,
                    isLoading = false,
                )

                is Msg.LeaderboardsLoading -> copy(isLoading = true)
                is Msg.Error -> copy(errorText = msg.message, isLoading = false)
                is Msg.LanguageChanged -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(selectedLanguage = msg.language),
                )

                is Msg.HireableChanged -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(hireable = msg.hireable),
                )

                is Msg.CountryCodeChanged -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(selectedCountryCode = msg.countryCode),
                )

                is Msg.FilterBottomSheetToggled -> copy(
                    filterBottomSheetState = filterBottomSheetState.copy(active = msg.active),
                )
            }
    }

    private companion object {
        const val STORE_NAME = "LeaderboardsStore"
    }
}
