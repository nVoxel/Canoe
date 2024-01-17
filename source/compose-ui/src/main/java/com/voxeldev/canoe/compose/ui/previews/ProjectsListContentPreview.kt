package com.voxeldev.canoe.compose.ui.previews

import androidx.compose.runtime.Composable
import com.voxeldev.canoe.compose.ui.content.projects.ProjectsListContent
import com.voxeldev.canoe.compose.ui.theme.CanoeTheme
import com.voxeldev.canoe.projects.api.Project
import com.voxeldev.canoe.projects.api.ProjectsModel
import com.voxeldev.canoe.projects.list.ProjectsList

/**
 * @author nvoxel
 */
@Composable
internal fun ProjectsListContentPreview() {
    val model = ProjectsList.Model(
        projectsModel = ProjectsModel(
            data = listOf(
                Project(
                    id = "1",
                    name = "Test Project 1",
                    repository = null,
                    badge = null,
                    color = null,
                    hasPublicUrl = false,
                    humanReadableLastHeartBeatAt = "1970-01-01T00:00:00Z",
                    lastHeartBeatAt = null,
                    url = "",
                    urlEncodedName = "",
                    createdAt = "1970-01-01T00:00:00Z",
                ),
                Project(
                    id = "2",
                    name = "Test Project 2",
                    repository = null,
                    badge = null,
                    color = null,
                    hasPublicUrl = false,
                    humanReadableLastHeartBeatAt = "1970-01-02T00:00:00Z",
                    lastHeartBeatAt = null,
                    url = "",
                    urlEncodedName = "",
                    createdAt = "1970-01-02T00:00:00Z",
                ),
                Project(
                    id = "3",
                    name = "Test Project 3",
                    repository = null,
                    badge = null,
                    color = null,
                    hasPublicUrl = false,
                    humanReadableLastHeartBeatAt = "1970-01-03T00:00:00Z",
                    lastHeartBeatAt = null,
                    url = "",
                    urlEncodedName = "",
                    createdAt = "1970-01-03T00:00:00Z",
                ),
            ),
        ),
        errorText = null,
        isLoading = false,
        searchActive = true,
        searchText = "Decompose",
    )

    CanoeTheme {
        ProjectsListContent(
            model = model,
            onToggleSearchClicked = {},
            onSearchButtonClicked = {},
            onTextChanged = {},
            onItemClicked = {},
            retryCallback = {},
        )
    }
}
