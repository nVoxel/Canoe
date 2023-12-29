package com.voxeldev.canoe.leaderboards.store

import com.arkivanov.mvikotlin.core.store.Store
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.Intent
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.BRAZIL
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.CHINA
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.GERMANY
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.INDIA
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.JAPAN
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.RUSSIA
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry.US
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.CSHARP
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.GO
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.JAVA
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.JAVASCRIPT
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.KOTLIN
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.PHP
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage.PYTHON
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.State

/**
 * @author nvoxel
 */
internal interface LeaderboardsStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        data object ToggleFilterBottomSheet : Intent()
        data object ReloadLeaderboards : Intent()
        data class SetLanguage(val language: String) : Intent()
        data class SetHireable(val hireable: Boolean) : Intent()
        data class SetCountryCode(val countryCode: String) : Intent()
        data object ResetFilters : Intent()
    }

    data class State(
        val leaderboardsModel: LeaderboardsModel? = null,
        val errorText: String? = null,
        val isLoading: Boolean = true,
        val filterBottomSheetState: FilterBottomSheetState = FilterBottomSheetState(),
    )

    data class FilterBottomSheetState(
        val active: Boolean = false,
        val selectedLanguage: String? = null,
        val languages: List<PredefinedLanguage> = listOf(
            JAVASCRIPT,
            PYTHON,
            GO,
            JAVA,
            KOTLIN,
            PHP,
            CSHARP
        ),
        val hireable: Boolean? = null,
        val selectedCountryCode: String? = null,
        val countryCodes: List<PredefinedCountry> = listOf(
            INDIA,
            CHINA,
            US,
            BRAZIL,
            RUSSIA,
            JAPAN,
            GERMANY
        ),
    )

    enum class PredefinedLanguage(val value: String) {
        JAVASCRIPT("JavaScript"),
        PYTHON("Python"),
        GO("GO"),
        JAVA("Java"),
        KOTLIN("Kotlin"),
        PHP("PHP"),
        CSHARP("C#"),
    }

    enum class PredefinedCountry(val value: String) {
        INDIA("IN"),
        CHINA("CN"),
        US("US"),
        BRAZIL("BR"),
        RUSSIA("RU"),
        JAPAN("JP"),
        GERMANY("DE"),
    }
}
