package com.example.jobseeker.navigations.graphs

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.NotificationRouteScreen
import com.example.jobseeker.screens.notification.NotificationDetailScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel

/**
 * Created 28-02-2024 at 03:05 pm
 */

fun NavGraphBuilder.notificationNavGraph(rootNavController: NavHostController, jobseekerViewModel: JobseekerViewModel, windowSize : WindowSize){

    navigation(
        route = Graph.NotificationGraph,
        startDestination = NotificationRouteScreen.NotificationDetail.route
    ){
        composable(
            route =  NotificationRouteScreen.NotificationDetail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ){


//            val notificationId = rootNavController.previousBackStackEntry?.arguments?.getString("id") ?: "0"
            NotificationDetailScreen(navController = rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize)
        }
    }
}