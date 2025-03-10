package com.example.jobseeker.navigations.graphs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.*
import com.example.jobseeker.screens.auth.ForgotPasswordScreen
import com.example.jobseeker.screens.auth.JobseekerLoginScreen
import com.example.jobseeker.screens.auth.JobseekerRegisterScreen
import com.example.jobseeker.screens.auth.RecruiterForgotPasswordScreen
import com.example.jobseeker.screens.auth.RecruiterLoginScreen
import com.example.jobseeker.screens.auth.RecruiterRegisterScreen
import com.example.jobseeker.screens.auth.StartAuthScreen
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

@RequiresApi(Build.VERSION_CODES.P)
fun NavGraphBuilder.authNavGraph(rootNavController: NavHostController, jobseekerViewModel: JobseekerViewModel, recruiterViewModel: RecruiterViewModel) {
    navigation(
        route = Graph.AuthGraph,
        startDestination = AuthRouteScreen.StartAuth.route
    ) {
        composable(route = AuthRouteScreen.StartAuth.route) {
            StartAuthScreen(navController = rootNavController)
        }
        composable(route = AuthRouteScreen.JobseekerLogin.route) {
            JobseekerLoginScreen(navController = rootNavController, jobseekerViewModel = jobseekerViewModel)
        }
        composable(route = AuthRouteScreen.JobseekerSignUp.route) {
            JobseekerRegisterScreen(navController = rootNavController, jobseekerViewModel = jobseekerViewModel)
        }
        composable(route = AuthRouteScreen.RecruiterLogin.route) {
            RecruiterLoginScreen(navController = rootNavController, recruiterViewModel = recruiterViewModel)
        }
        composable(route = AuthRouteScreen.RecruiterSignUp.route) {
            RecruiterRegisterScreen(navController = rootNavController, recruiterViewModel = recruiterViewModel)
        }
        composable(route = AuthRouteScreen.Forget.route) {
            ForgotPasswordScreen(navController = rootNavController)
        }
        composable(route = AuthRouteScreen.RecruiterForget.route) {
            RecruiterForgotPasswordScreen(navController = rootNavController)
        }
    }
}