package com.voxeldev.canoe.compose.ui.content.projects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.voxeldev.canoe.compose.ui.components.Error
import com.voxeldev.canoe.compose.ui.components.Loader
import com.voxeldev.canoe.compose.ui.previews.ProjectsListContentPreview
import com.voxeldev.canoe.projects.api.Project
import com.voxeldev.canoe.projects.list.ProjectsList
import com.voxeldev.canoe.projects.list.ProjectsListComponent

/**
 * @author nvoxel
 */
@Composable
internal fun ProjectsListContent(component: ProjectsListComponent) {
    with(component) {
        val model by model.subscribeAsState()

        ProjectsListContent(
            model = model,
            onToggleSearchClicked = ::onToggleSearchClicked,
            onSearchButtonClicked = ::onSearchClicked,
            onTextChanged = ::onSearchTextChanged,
            onItemClicked = ::onItemClicked,
            retryCallback = ::onSearchClicked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProjectsListContent(
    model: ProjectsList.Model,
    onToggleSearchClicked: () -> Unit,
    onSearchButtonClicked: () -> Unit,
    onTextChanged: (String) -> Unit,
    onItemClicked: (id: String) -> Unit,
    retryCallback: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Projects") },
                actions = {
                    IconButton(onClick = onToggleSearchClicked) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                },
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding()),
            ) {
                model.errorText?.let {
                    Error(
                        message = it,
                        shouldShowRetry = true,
                        retryCallback = retryCallback,
                    )
                } ?: run {
                    SearchField(
                        isVisible = model.searchActive,
                        text = model.searchText,
                        onTextChanged = onTextChanged,
                        onSearchButtonClicked = onSearchButtonClicked,
                    )

                    ProjectsList(
                        isLoading = model.isLoading,
                        projects = model.projectsModel.data,
                        onItemClicked = onItemClicked,
                    )
                }
            }
        },
    )
}

@Composable
private fun SearchField(
    isVisible: Boolean,
    text: String,
    onTextChanged: (String) -> Unit,
    onSearchButtonClicked: () -> Unit,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = text,
                onValueChange = onTextChanged,
                label = { Text(text = "Project to search") },
                trailingIcon = {
                    IconButton(onClick = onSearchButtonClicked) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                },
            )
        }
    }
}

@Composable
private fun ProjectsList(
    isLoading: Boolean,
    projects: List<Project>,
    onItemClicked: (id: String) -> Unit,
) {
    if (isLoading) Loader()

    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        items(
            items = projects,
            key = { project -> project.id },
        ) { project ->
            ProjectsListItem(project = project, onItemClicked = onItemClicked)
        }
    }
}

@Composable
private fun ProjectsListItem(
    project: Project,
    onItemClicked: (id: String) -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
            .clickable { onItemClicked(project.name) },
    ) {
        Column(
            modifier = Modifier
                .padding(all = 32.dp)
                .fillMaxWidth(),
        ) {
            Text(text = project.name)
            Text(text = "Created at: ${project.createdAt}")
            project.humanReadableLastHeartBeatAt?.let { Text(text = "Last update at: $it") }
        }
    }
}

@Preview
@Composable
private fun Preview() = ProjectsListContentPreview()
