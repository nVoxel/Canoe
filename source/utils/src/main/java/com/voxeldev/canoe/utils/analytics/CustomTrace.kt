package com.voxeldev.canoe.utils.analytics

import com.google.firebase.perf.FirebasePerformance

/**
 * @author nvoxel
 */
sealed class CustomTrace(
    val name: String,
) {

    data object SummariesLoadTrace : CustomTrace(name = "summaries_loading")
    data object ProgramLanguagesLoadTrace : CustomTrace(name = "languages_loading")
    data object AllTimeLoadTrace : CustomTrace(name = "all_time_loading")
    data object LeaderboardsLoadTrace : CustomTrace(name = "leaderboards_loading")
    data object ProjectsLoadTrace : CustomTrace(name = "projects_loading")
    data object AuthenticationLoadTrace : CustomTrace(name = "authentication_loading")
}

fun FirebasePerformance.startTrace(trace: CustomTrace) = newTrace(trace.name).also { it.start() }
