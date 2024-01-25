package com.voxeldev.canoe.compose.ui.previews

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.voxeldev.canoe.compose.ui.content.leaderboards.LeaderboardsContent
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.leaderboards.Leaderboards
import com.voxeldev.canoe.leaderboards.api.CurrentUser
import com.voxeldev.canoe.leaderboards.api.LeaderboardEntry
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.TimeRange
import com.voxeldev.canoe.leaderboards.api.User
import com.voxeldev.canoe.utils.providers.string.StringResourceProvider

/**
 * @author nvoxel
 */
@Preview
@Composable
internal fun LeaderboardsContentPreview() {
    val model = Leaderboards.Model(
        leaderboardsModel = LeaderboardsModel(
            currentUser = CurrentUser(rank = 123, user = User(id = "", username = "Test User")),
            data = listOf(
                LeaderboardEntry(rank = 1, runningTotal = null, user = User(id = "", username = "Leader 1")),
                LeaderboardEntry(rank = 2, runningTotal = null, user = User(id = "", username = "Leader 2")),
                LeaderboardEntry(rank = 3, runningTotal = null, user = User(id = "", username = "Leader 3")),
                LeaderboardEntry(rank = 4, runningTotal = null, user = User(id = "", username = "Leader 4")),
                LeaderboardEntry(rank = 5, runningTotal = null, user = User(id = "", username = "Leader 5")),
            ),
            page = 0,
            totalPages = 0,
            range = TimeRange(),
            modifiedAt = "",
            timeout = 0,
            writesOnly = false,
        ),
        isLoading = false,
        errorText = null,
        filterBottomSheetModel = Leaderboards.FilterBottomSheetModel(
            active = false,
            selectedLanguage = null,
            languages = listOf(),
            hireable = null,
            selectedCountryCode = null,
            countryCodes = listOf(),
        ),
    )

    val stringResourceProvider = object : StringResourceProvider {
        override fun getWakaTimeApiBaseUrl() = ""
        override fun getWakaTimeOAuthBaseUrl() = ""
        override fun getWakaTimePhotoBaseUrl() = ""
        override fun getWakaTimeProfileBaseUrl() = ""
        override fun getOAuthAuthorizeUrl() = ""
        override fun getOAuthClientId() = ""
        override fun getOAuthClientSecret() = ""
        override fun getOAuthRedirectUrl() = ""
        override fun getJavascriptString() = ""
        override fun getPythonString() = ""
        override fun getGoString() = ""
        override fun getJavaString() = ""
        override fun getKotlinString() = ""
        override fun getPhpString() = ""
        override fun getCSharpString() = ""
        override fun getIndiaString() = ""
        override fun getChinaString() = ""
        override fun getUsString() = ""
        override fun getBrazilString() = ""
        override fun getRussiaString() = ""
        override fun getJapanString() = ""
        override fun getGermanyString() = ""
    }

    CanoeTheme {
        LeaderboardsContent(
            model = model,
            stringResourceProvider = stringResourceProvider,
            onItemClicked = {},
            onToggleFilterBottomSheet = {},
            onDismissRequest = {},
            onSelectLanguage = {},
            onSelectHireable = {},
            onSelectCountry = {},
            onResetFilters = {},
            retryCallback = {},
        )
    }
}
