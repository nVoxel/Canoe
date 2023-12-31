package com.voxeldev.canoe.settings.store

import android.net.Uri
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.perf.performance
import com.voxeldev.canoe.settings.integration.GetAccessTokenFromCodeUseCase
import com.voxeldev.canoe.settings.integration.GetAccessTokenFromStorageUseCase
import com.voxeldev.canoe.settings.integration.RevokeAccessTokenUseCase
import com.voxeldev.canoe.settings.store.SettingsStore.Intent
import com.voxeldev.canoe.settings.store.SettingsStore.State
import com.voxeldev.canoe.utils.analytics.CustomEvent
import com.voxeldev.canoe.utils.analytics.CustomTrace
import com.voxeldev.canoe.utils.analytics.logEvent
import com.voxeldev.canoe.utils.analytics.startTrace
import com.voxeldev.canoe.utils.integration.BaseUseCase
import com.voxeldev.canoe.utils.parsers.AuthenticationCodeParser

/**
 * @author nvoxel
 */
internal class SettingsStoreProvider(
    private val storeFactory: StoreFactory,
    private val deepLink: Uri?,
    private val authenticationCodeParser: AuthenticationCodeParser,
    private val firebaseAnalytics: FirebaseAnalytics = Firebase.analytics,
    private val getAccessTokenFromCodeUseCase: GetAccessTokenFromCodeUseCase = GetAccessTokenFromCodeUseCase(),
    private val getAccessTokenFromStorageUseCase: GetAccessTokenFromStorageUseCase = GetAccessTokenFromStorageUseCase(),
    private val revokeAccessTokenUseCase: RevokeAccessTokenUseCase = RevokeAccessTokenUseCase(),
) {

    fun provide(): SettingsStore =
        object :
            SettingsStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = STORE_NAME,
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed class Msg {
        data class AccessTokenLoaded(val isConnected: Boolean) : Msg()
        data object AccessTokenLoading : Msg()
        data class Error(val message: String) : Msg()
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Unit, State, Msg, Nothing>() {

        override fun executeAction(action: Unit, getState: () -> State) {
            getAccessTokenFromStorage()
        }

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.DisconnectAccount -> revokeAccessToken()
                is Intent.ReloadAccount -> getAccessTokenFromStorage()
            }
        }

        private fun getAccessTokenFromStorage() {
            dispatch(Msg.AccessTokenLoading)
            getAccessTokenFromStorageUseCase(params = BaseUseCase.NoParams, scope = scope) { result ->
                result.fold(
                    onSuccess = { dispatch(message = Msg.AccessTokenLoaded(isConnected = true)) },
                    onFailure = {
                        deepLink?.let { getAccessTokenFromCode(it) } ?: dispatch(message = Msg.AccessTokenLoaded(isConnected = false))
                    },
                )
            }
        }

        private fun getAccessTokenFromCode(uri: Uri) {
            authenticationCodeParser.getAuthenticationCode(uri)?.let { code ->
                val trace = Firebase.performance.startTrace(trace = CustomTrace.AuthenticationLoadTrace)
                dispatch(message = Msg.AccessTokenLoading)
                getAccessTokenFromCodeUseCase(params = code, scope = scope) { result ->
                    result
                        .fold(
                            onSuccess = {
                                firebaseAnalytics.logEvent(event = CustomEvent.Login)
                                dispatch(message = Msg.AccessTokenLoaded(isConnected = true))
                            },
                            onFailure = { dispatch(message = Msg.Error(message = it.message ?: it.toString())) },
                        )
                        .also { trace.stop() }
                }
            }
        }

        private fun revokeAccessToken() {
            val trace = Firebase.performance.startTrace(trace = CustomTrace.AuthenticationLoadTrace)
            dispatch(Msg.AccessTokenLoading)
            revokeAccessTokenUseCase(params = BaseUseCase.NoParams, scope = scope) { result ->
                result
                    .fold(
                        onSuccess = {
                            firebaseAnalytics.logEvent(event = CustomEvent.Logout)
                            dispatch(message = Msg.AccessTokenLoaded(isConnected = false))
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
                is Msg.AccessTokenLoaded -> copy(isConnected = msg.isConnected, errorText = null, isLoading = false)
                is Msg.AccessTokenLoading -> copy(isLoading = true)
                is Msg.Error -> copy(errorText = msg.message, isLoading = false)
            }
    }

    private companion object {
        const val STORE_NAME = "SettingsStore"
    }
}
