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
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeModel
import com.voxeldev.canoe.dashboard.api.alltime.AllTimeRequest
import com.voxeldev.canoe.dashboard.api.languages.ProgramLanguagesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesModel
import com.voxeldev.canoe.dashboard.api.sumaries.SummariesRequest
import com.voxeldev.canoe.dashboard.integration.GetAllTimeUseCase
import com.voxeldev.canoe.dashboard.integration.GetProgramLanguagesUseCase
import com.voxeldev.canoe.dashboard.integration.GetSummariesUseCase
import com.voxeldev.canoe.dashboard.store.DashboardStore.Intent
import com.voxeldev.canoe.dashboard.store.DashboardStore.State
import com.voxeldev.canoe.utils.analytics.CustomEvent
import com.voxeldev.canoe.utils.analytics.CustomTrace
import com.voxeldev.canoe.utils.analytics.logEvent
import com.voxeldev.canoe.utils.analytics.startTrace
import com.voxeldev.canoe.utils.extensions.getMessage
import com.voxeldev.canoe.utils.integration.BaseUseCase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * @author nvoxel
 */
internal class DashboardStoreProvider(
    private val projectName: String?,
    private val storeFactory: StoreFactory,
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics,
    private val getProgramLanguagesUseCase: GetProgramLanguagesUseCase = GetProgramLanguagesUseCase(),
    private val getSummariesUseCase: GetSummariesUseCase = GetSummariesUseCase(),
    private val getAllTimeUseCase: GetAllTimeUseCase = GetAllTimeUseCase(),
) {

    fun provide(): DashboardStore =
        object :
            DashboardStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME + (projectName ?: ""),
                initialState = State(projectName = projectName),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class SummariesLoaded(val summariesModel: SummariesModel) : Msg()
        data object SummariesLoading : Msg()
        data class ProgramLanguagesLoaded(val programLanguagesModel: ProgramLanguagesModel) : Msg()
        data object ProgramLanguagesLoading : Msg()
        data class AllTimeLoaded(val allTimeModel: AllTimeModel) : Msg()
        data object AllTimeLoading : Msg()
        data class SummariesError(val message: String) : Msg()
        data class ProgramLanguagesError(val message: String) : Msg()
        data class AllTimeError(val message: String) : Msg()
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
                    params = getSummariesRequest(getState().datePickerBottomSheetState),
                )

                is Intent.DatesReset -> {
                    dispatch(message = Msg.DatesReset)
                    loadDashboard(params = getSummariesRequest(state = getState().datePickerBottomSheetState))
                }

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
                            if (startDate == null || endDate == null) {
                                return@let
                            }

                            dispatch(message = Msg.StartDateChanged(startDate = startDate))
                            dispatch(message = Msg.EndDateChanged(endDate = endDate))

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
                        onFailure = { dispatch(message = Msg.SummariesError(message = it.getMessage())) },
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
                            dispatch(message = Msg.ProgramLanguagesError(message = it.getMessage()))
                        },
                    )
                    .also { programLanguagesTrace.stop() }
            }

            val allTimeTrace = Firebase.performance.startTrace(trace = CustomTrace.AllTimeLoadTrace)
            dispatch(message = Msg.AllTimeLoading)
            getAllTimeUseCase(params = getAllTimeRequest()) { result ->
                result.fold(
                    onSuccess = {
                        firebaseAnalytics.logEvent(event = CustomEvent.LoadedAllTime)
                        dispatch(message = Msg.AllTimeLoaded(allTimeModel = it))
                    },
                    onFailure = {
                        dispatch(message = Msg.AllTimeError(message = it.getMessage()))
                    },
                ).also { allTimeTrace.stop() }
            }
        }

        private fun getSummariesRequest(state: DashboardStore.DatePickerBottomSheetState) =
            SummariesRequest(
                startDate = state.startDate,
                endDate = state.endDate,
                project = projectName,
            )

        private fun getAllTimeRequest() =
            AllTimeRequest(
                project = projectName,
            )

        private fun Long.toDateFormat(): String {
            formatCalendar.timeInMillis = this
            return simpleDateFormat.format(formatCalendar.time)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.SummariesLoaded -> copy(
                    summariesModel = msg.summariesModel,
                    isSummariesLoading = false,
                )

                is Msg.SummariesLoading -> copy(isSummariesLoading = true)

                is Msg.ProgramLanguagesLoaded -> copy(
                    programLanguagesModel = msg.programLanguagesModel,
                    isProgramLanguagesLoading = false,
                )

                is Msg.ProgramLanguagesLoading -> copy(isProgramLanguagesLoading = true)

                is Msg.AllTimeLoaded -> copy(
                    allTimeModel = msg.allTimeModel,
                    isAllTimeLoading = false,
                )

                is Msg.AllTimeLoading -> copy(isAllTimeLoading = true)

                is Msg.SummariesError -> copy(errorText = msg.message, isSummariesLoading = false)
                is Msg.ProgramLanguagesError -> copy(errorText = msg.message, isProgramLanguagesLoading = false)
                is Msg.AllTimeError -> copy(errorText = msg.message, isAllTimeLoading = false)

                is Msg.StartDateChanged -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(startDate = msg.startDate),
                )

                is Msg.EndDateChanged -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(endDate = msg.endDate),
                )

                is Msg.DatesReset -> copy(datePickerBottomSheetState = DashboardStore.DatePickerBottomSheetState())
                is Msg.DatePickerBottomSheetShowed -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(active = true),
                )
                is Msg.DatePickerBottomSheetDismissed -> copy(
                    datePickerBottomSheetState = datePickerBottomSheetState.copy(active = false),
                )
            }
    }

    private companion object {
        const val STORE_NAME = "DashboardStore"

        const val ISO_8601_DATE_FORMAT = "yyyy-MM-dd"
    }
}
