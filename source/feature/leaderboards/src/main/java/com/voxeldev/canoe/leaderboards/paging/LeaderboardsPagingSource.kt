package com.voxeldev.canoe.leaderboards.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.Firebase
import com.google.firebase.perf.performance
import com.voxeldev.canoe.leaderboards.api.LeaderboardEntry
import com.voxeldev.canoe.leaderboards.api.LeaderboardsModel
import com.voxeldev.canoe.leaderboards.api.LeaderboardsRequest
import com.voxeldev.canoe.leaderboards.integration.GetLeaderboardsSyncUseCase
import com.voxeldev.canoe.leaderboards.store.LeaderboardsStore
import com.voxeldev.canoe.utils.analytics.CustomTrace
import com.voxeldev.canoe.utils.analytics.startTrace
import com.voxeldev.canoe.utils.exceptions.PageNotFoundException

/**
 * @author nvoxel
 */
internal class LeaderboardsPagingSource(
    private val state: LeaderboardsStore.State,
    private val getLeaderboardsSyncUseCase: GetLeaderboardsSyncUseCase = GetLeaderboardsSyncUseCase(),
) : PagingSource<Int, LeaderboardEntry>() {

    override fun getRefreshKey(state: PagingState<Int, LeaderboardEntry>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LeaderboardEntry> {
        val currentPage = params.key ?: START_PAGE

        val trace = Firebase.performance.startTrace(trace = CustomTrace.LeaderboardsLoadTrace)

        return getLeaderboardsSyncUseCase(
            params = getLeaderboardsRequest(
                currentPage = currentPage,
                currentState = state,
            ),
        ).fold(
            onSuccess = { model ->
                if (model.data.isEmpty()) {
                    LoadResult.Error(PageNotFoundException())
                } else {
                    modelToPage(
                        currentPage = currentPage,
                        model = model,
                    )
                }
            },
            onFailure = {
                LoadResult.Error(PageNotFoundException())
            },
        ).also { trace.stop() }
    }

    private fun getLeaderboardsRequest(currentPage: Int, currentState: LeaderboardsStore.State): LeaderboardsRequest =
        LeaderboardsRequest(
            language = currentState.filterBottomSheetState.selectedLanguage,
            isHireable = currentState.filterBottomSheetState.hireable,
            countryCode = currentState.filterBottomSheetState.selectedCountryCode,
            page = currentPage,
        )

    private fun modelToPage(currentPage: Int, model: LeaderboardsModel): LoadResult.Page<Int, LeaderboardEntry> {
        val nextPage = currentPage + ADD_PAGE
        val itemsBefore = (currentPage - ADD_PAGE) * PAGING_PAGE_SIZE
        val itemsAfter =
            if (nextPage == model.totalPages) model.data.size else (model.totalPages - currentPage) * PAGING_PAGE_SIZE

        return LoadResult.Page(
            data = model.data,
            prevKey = null,
            nextKey = if (nextPage <= model.totalPages) nextPage else null,
            itemsBefore = itemsBefore,
            itemsAfter = itemsAfter,
        )
    }

    companion object {
        private const val START_PAGE = 1
        private const val ADD_PAGE = 1
        const val PAGING_PAGE_SIZE = 100
        const val PAGING_MAX_SIZE = 300
    }
}
