package com.example.jobseeker.navigations.graphs

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.screens.applicant.ApplicationModule
import com.example.jobseeker.screens.main.RecruiterNotificationScreen
import com.example.jobseeker.screens.main.RecuiterHomeScreen
import com.example.jobseeker.screens.main.RecuiterProfileScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

@Composable
fun RecruiterMainNavGraph(
    rootNavController: NavHostController,
    homeNavController: NavHostController,
    innerPadding: PaddingValues,
    jobseekerViewModel: JobseekerViewModel,
    recruiterViewModel: RecruiterViewModel,
    windowSize : WindowSize
) {
    NavHost(
        navController = homeNavController,
        route = Graph.RecruiterMainScreenGraph,
        startDestination = MainRouteScreen.recruiterHome.route
    ) {
        composable(route = MainRouteScreen.recruiterHome.route){
            RecuiterHomeScreen(innerPadding = innerPadding, rootNavController = rootNavController, windowSize = windowSize)
        }
        composable(route = MainRouteScreen.recruiterNotification.route){
            RecruiterNotificationScreen(innerPadding = innerPadding, rootNavController = rootNavController, recruiterViewModel = recruiterViewModel, windowSize = windowSize)
        }
        composable(route = MainRouteScreen.recruiterProfile.route){
            RecuiterProfileScreen(innerPadding = innerPadding, rootNavController = rootNavController, recruiterViewModel = recruiterViewModel,  windowSize = windowSize)
        }

    }
}