package com.example.jobseeker.navigations.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.JobDetailRouteScreen
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.screens.jobDetail.ApplySuccessScreen
import com.example.jobseeker.screens.jobDetail.FilteringResultScreen
import com.example.jobseeker.screens.jobDetail.JobDetailScreen
import com.example.jobseeker.screens.jobDetail.JobFilteringScreen
import com.example.jobseeker.screens.main.JobseekerHomeScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel

/**
 * Created 28-02-2024 at 03:05 pm
 */

fun NavGraphBuilder.jobDetailNavGraph(rootNavController: NavHostController, jobseekerViewModel: JobseekerViewModel, windowSize: WindowSize){
    navigation(
        route = Graph.JobDetailGraph,
        startDestination = JobDetailRouteScreen.JobDetail.route
    ){
        composable(
            route = JobDetailRouteScreen.JobDetail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                    defaultValue = "0"
                }
            )
        ){
            JobDetailScreen(navController = rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize)
        }
        composable(route = JobDetailRouteScreen.ApplySuccess.route) {
            ApplySuccessScreen(navController = rootNavController)
        }
        composable(route = JobDetailRouteScreen.JobFiltering.route) {
            JobFilteringScreen(navController = rootNavController, jobseekerViewModel = jobseekerViewModel)
        }
        composable(route = JobDetailRouteScreen.FilteringResult.route) {
            FilteringResultScreen(navController = rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize)
        }
    }
}