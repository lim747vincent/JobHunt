package com.example.jobseeker.navigations.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.screens.main.JobseekerActivityScreen
import com.example.jobseeker.screens.main.JobseekerHomeScreen
import com.example.jobseeker.screens.main.JobseekerNotificationScreen
import com.example.jobseeker.screens.main.JobseekerProfileScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

/**
 * Created 28-02-2024 at 03:05 pm
 */

@Composable
fun MainNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    innerPadding: PaddingValues,
    jobseekerViewModel: JobseekerViewModel, windowSize: WindowSize
) {
    NavHost(
        navController = homeNavController,
        route = Graph.MainScreenGraph,
        startDestination = MainRouteScreen.Home.route
    ) {
        composable(route = MainRouteScreen.Home.route) {
            JobseekerHomeScreen(rootNavController = rootNavController, innerPadding = innerPadding, jobseekerViewModel = jobseekerViewModel, windowSize= windowSize)
        }
        composable(route = MainRouteScreen.Activity.route) {
            JobseekerActivityScreen(rootNavController = rootNavController, innerPadding = innerPadding, jobseekerViewModel = jobseekerViewModel,  windowSize= windowSize)
        }
        composable(route = MainRouteScreen.Notification.route) {
            JobseekerNotificationScreen(rootNavController = rootNavController, innerPadding = innerPadding, jobseekerViewModel = jobseekerViewModel, windowSize= windowSize)
        }
        composable(route = MainRouteScreen.Profile.route) {
            JobseekerProfileScreen(innerPadding = innerPadding, rootNavController = rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize= windowSize)
        }


    }
}
