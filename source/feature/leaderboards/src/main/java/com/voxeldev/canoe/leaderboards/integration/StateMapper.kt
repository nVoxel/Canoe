package com.voxeldev.canoe.leaderboards.integration

import com.voxeldev.canoe.leaderboards.Leaderboards
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedCountry
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.PredefinedLanguage
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore.State
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * @author nvoxel
 */
internal class StateMapper : KoinComponent {

    private val stringResourceProvider: StringResourceProvider by inject()

    fun toModel(state: State): Leaderboards.Model =
        Leaderboards.Model(
            leaderboardsModel = state.leaderboardsModel,
            leaderboardsFlow = state.leaderboardsFlow,
            errorText = state.errorText,
            isLoading = state.isLoading,
            filterBottomSheetModel = Leaderboards.FilterBottomSheetModel(
                active = state.filterBottomSheetState.active,
                selectedLanguage = state.filterBottomSheetState.selectedLanguage,
                languages = state.filterBottomSheetState.languages.map { it.toModel() },
                hireable = state.filterBottomSheetState.hireable,
                selectedCountryCode = state.filterBottomSheetState.selectedCountryCode,
                countryCodes = state.filterBottomSheetState.countryCodes.map { it.toModel() },
            ),
        )

    private fun PredefinedLanguage.toModel() =
        when (this) {
            PredefinedLanguage.JAVASCRIPT -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getJavascriptString(),
                value = value,
            )
            PredefinedLanguage.PYTHON -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getPythonString(),
                value = value,
            )
            PredefinedLanguage.GO -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getGoString(),
                value = value,
            )
            PredefinedLanguage.JAVA -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getJavaString(),
                value = value,
            )
            PredefinedLanguage.KOTLIN -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getKotlinString(),
                value = value,
            )
            PredefinedLanguage.PHP -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getPhpString(),
                value = value,
            )
            PredefinedLanguage.CSHARP -> Leaderboards.PredefinedLanguageModel(
                name = stringResourceProvider.getCSharpString(),
                value = value,
            )
        }

    private fun PredefinedCountry.toModel() =
        when (this) {
            PredefinedCountry.INDIA -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getIndiaString(),
                value = value,
            )
            PredefinedCountry.CHINA -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getChinaString(),
                value = value,
            )
            PredefinedCountry.US -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getUsString(),
                value = value,
            )
            PredefinedCountry.BRAZIL -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getBrazilString(),
                value = value,
            )
            PredefinedCountry.RUSSIA -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getRussiaString(),
                value = value,
            )
            PredefinedCountry.JAPAN -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getJapanString(),
                value = value,
            )
            PredefinedCountry.GERMANY -> Leaderboards.PredefinedCountryModel(
                name = stringResourceProvider.getGermanyString(),
                value = value,
            )
        }
}
