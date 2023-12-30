package com.voxeldev.canoe.dashboard.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.perf.performance
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRequest
import com.voxeldev.canoe.dashboard.integration.GetProgramLanguagesUseCase
import com.voxeldev.canoe.dashboard.integration.GetSummariesUseCase
import com.voxeldev.canoe.dashboard.store.DashboardStore.Intent
import com.voxeldev.canoe.dashboard.store.DashboardStore.State
import com.voxeldev.canoe.utils.analytics.CustomEvent
import com.voxeldev.canoe.utils.analytics.CustomTrace
import com.voxeldev.canoe.utils.analytics.logEvent
import com.voxeldev.canoe.utils.analytics.startTrace
import com.voxeldev.canoe.utils.integration.BaseUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * @author nvoxel
 */
internal class DashboardStoreProvider(
    private val storeFactory: StoreFactory,
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics,
    private val getProgramLanguagesUseCase: GetProgramLanguagesUseCase = GetProgramLanguagesUseCase(),
    private val getSummariesUseCase: GetSummariesUseCase = GetSummariesUseCase(),
) {

    fun provide(): DashboardStore =
        object :
            DashboardStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class SummariesLoaded(val summariesModel: SummariesModel) : Msg()
        data object SummariesLoading : Msg()
        data class ProgramLanguagesLoaded(val programLanguagesModel: ProgramLanguagesModel) : Msg()
        data object ProgramLanguagesLoading : Msg()
        data class SummariesError(val message: String) : Msg()
        data class ProgramLanguagesError(val message: String) : Msg()
        data class StartDateChanged(val startDate: String) : Msg()
        data class EndDateChanged(val endDate: String) : Msg()
        data object DatesReset : Msg()
        data object DatePickerBottomSheetShowed : Msg()
        data object DatePickerBottomSheetDismissed : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {
        private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(ISO_8601_DATE_FORMAT, Locale.getDefault())
        private val formatCalendar: Calendar = Calendar.getInstance()

        private var lastDatePickerState: DashboardStore.DatePickerBottomSheetState? = null

        override fun executeAction(action: Unit, getState: () -> State) {
            loadDashboard(params = getSummariesRequest(getState().datePickerBottomSheetState))
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ReloadDashboard -> loadDashboard(
                    params = getSummariesRequest(getState().datePickerBottomSheetState)
                )

                is Intent.ShowDatePickerBottomSheet -> {
                    val state = getState()
                    lastDatePickerState = state.datePickerBottomSheetState.copy()
                    dispatch(message = Msg.DatePickerBottomSheetShowed)
                }

                is Intent.DismissDatePickerBottomSheet -> {
                    val startDate = intent.startMillis?.toDateFormat()
                    val endDate = intent.endMillis?.toDateFormat()

                    lastDatePickerState?.let { lastDatePickerState ->
                        if (startDate != lastDatePickerState.startDate || endDate != lastDatePickerState.endDate) {
                            if (startDate != null && endDate != null) {
                                dispatch(message = Msg.StartDateChanged(startDate = startDate))
                                dispatch(message = Msg.EndDateChanged(endDate = endDate))
                            } else {
                                dispatch(message = Msg.DatesReset)
                            }

                            loadDashboard(params = getSummariesRequest(state = getState().datePickerBottomSheetState))
                        }
                    }

                    dispatch(message = Msg.DatePickerBottomSheetDismissed)
                }
            }
        }

        private fun loadDashboard(params: SummariesRequest) {
            val summariesTrace = Firebase.performance.startTrace(trace = CustomTrace.SummariesLoadTrace)
            dispatch(message = Msg.SummariesLoading)
            getSummariesUseCase(params = params, scope = scope) { result ->
                result
                    .fold(
                        onSuccess = {
                            firebaseAnalytics.logEvent(event = CustomEvent.LoadedSummaries)
                            dispatch(message = Msg.SummariesLoaded(summariesModel = it))
                        },
                        onFailure = { dispatch(message = Msg.SummariesError(message = it.message ?: it.toString())) },
                    )
                    .also { summariesTrace.stop() }
            }

            val programLanguagesTrace = Firebase.performance.startTrace(trace = CustomTrace.ProgramLanguagesLoadTrace)
            dispatch(message = Msg.ProgramLanguagesLoading)
            getProgramLanguagesUseCase(params = BaseUseCase.NoParams) { result ->
                result
                    .fold(
                        onSuccess = {
                            firebaseAnalytics.logEvent(event = CustomEvent.LoadedProgramLanguages)
                            dispatch(message = Msg.ProgramLanguagesLoaded(programLanguagesModel = it))
                        },
                        onFailure = {
                            dispatch(message = Msg.ProgramLanguagesError(message = it.message ?: it.toString()))
                        },
                    )
                    .also { programLanguagesTrace.stop() }
            }
        }

        private fun getSummariesRequest(state: DashboardStore.DatePickerBottomSheetState) =
            SummariesRequest(
                startDate = state.startDate,
                endDate = state.endDate,
            )

        private fun Long.toDateFormat(): String {
            formatCalendar.timeInMillis = this
            return simpleDateFormat.format(formatCalendar.time)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SummariesLoaded -> copy(summariesModel = msg.summariesModel, isSummariesLoading = false)
                is Msg.SummariesLoading -> copy(isSummariesLoading = true)
                is Msg.ProgramLanguagesLoaded -> copy(
                    programLanguagesModel = msg.programLanguagesModel,
                    isProgramLanguagesLoading = false
                )

                is Msg.ProgramLanguagesLoading -> copy(isProgramLanguagesLoading = true)
                is Msg.SummariesError -> copy(errorText = msg.message, isSummariesLoading = false)
                is Msg.ProgramLanguagesError -> copy(errorText = msg.message, isProgramLanguagesLoading = false)
                is Msg.StartDateChanged -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(startDate = msg.startDate)
                )

                is Msg.EndDateChanged -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(endDate = msg.endDate)
                )

                is Msg.DatesReset -> copy(datePickerBottomSheetState = DashboardStore.DatePickerBottomSheetState())
                is Msg.DatePickerBottomSheetShowed -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(active = true)
                )

                is Msg.DatePickerBottomSheetDismissed -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(active = false)
                )
            }
    }

    private companion object {
        const val STORE_NAME = "DashboardStore"

        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd"
    }
}
