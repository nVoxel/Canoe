package com.voxeldev.canoe.leaderboards

import com.arkivanov.decompose.value.Value
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel

/**
 * @author nvoxel
 */
interface Leaderboards {

    val model: Value<Model>

    fun onItemClicked(profileUrl: String)

    fun onReloadClicked()

    fun onToggleFilterBottomSheet()

    fun onSelectLanguage(language: String)

    fun onSelectHireable(hireable: Boolean)

    fun onSelectCountryCode(countryCode: String)

    fun onResetFilters()

    data class Model(
        val leaderboardsModel: LeaderboardsModel?,
        val errorText: String?,
        val isLoading: Boolean,
        val filterBottomSheetModel: FilterBottomSheetModel,
    )

    data class FilterBottomSheetModel(
        val active: Boolean,
        val selectedLanguage: String?,
        val languages: List<PredefinedLanguageModel>,
        val hireable: Boolean?,
        val selectedCountryCode: String?,
        val countryCodes: List<PredefinedCountryModel>,
    )

    data class PredefinedLanguageModel(
        val name: String,
        val value: String,
    )

    data class PredefinedCountryModel(
        val name: String,
        val value: String,
    )

    sealed class Output {
        data class Selected(val profileUrl: String) : Output()
    }
}
