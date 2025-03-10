package com.example.jobseeker.navigations.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.NotificationRouteScreen
import com.example.jobseeker.navigations.RecruiterNotificationRouteScreen
import com.example.jobseeker.screens.notification.NotificationDetailScreen
import com.example.jobseeker.screens.notification.RecruiterNotificationDetailScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

fun NavGraphBuilder.recruiterNotificationNavGraph(rootNavController: NavHostController, recruiterViewModel: RecruiterViewModel, windowSize : WindowSize){

    navigation(
        route = Graph.RecruiterNotificationGraph,
        startDestination = RecruiterNotificationRouteScreen.RecruiterNotificationDetail.route
    ){
        composable(
            route =  RecruiterNotificationRouteScreen.RecruiterNotificationDetail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ){


//            val notificationId = rootNavController.previousBackStackEntry?.arguments?.getString("id") ?: "0"
            RecruiterNotificationDetailScreen(navController = rootNavController, recruiterViewModel = recruiterViewModel, windowSize = windowSize)
        }
    }
}