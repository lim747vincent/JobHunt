package com.example.jobseeker.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.component.BottomNavigationBar
import com.example.jobseeker.component.MyTopAppBar
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.model.NavigationItem
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.navigations.graphs.MainNavGraph
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel

/**
 * Created 28-02-2024 at 03:27 pm
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavHostController: NavHostController,
    homeNavController : NavHostController = rememberNavController(), jobseekerViewModel: JobseekerViewModel, windowSize: WindowSize
) {

    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    var badgeCount by remember { mutableStateOf(0) }

    LaunchedEffect(profileUiState.jobseeker_email) {
        val fetchedNotifications = jobseekerViewModel.getNotificationForJobseeker(profileUiState.jobseeker_email)
        jobseekerViewModel.getResume(profileUiState.jobseeker_email)
        badgeCount = fetchedNotifications.count { !it.jobseeker_notification_isRead }
    }

    val bottomNavigationItemsList = listOf(
        NavigationItem(
            title = "Home",
            route = MainRouteScreen.Home.route,
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = "Activity",
            route = MainRouteScreen.Activity.route,
            selectedIcon = Icons.Filled.Mail,
            unSelectedIcon = Icons.Outlined.Mail
        ),
        NavigationItem(
            title = "Notification",
            route = MainRouteScreen.Notification.route,
            selectedIcon = Icons.Filled.Notifications,
            unSelectedIcon = Icons.Outlined.Notifications,
            badgeCount = badgeCount // Get badge count from ViewModel
        ),
        NavigationItem(
            title = "Profile",
            route = MainRouteScreen.Profile.route,
            selectedIcon = Icons.Filled.Person,
            unSelectedIcon = Icons.Outlined.Person
        ),
    )




    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()

    // remember expect stable value. So cannot directly accept navBackStackEntry?.destination?.route parameter
    // Therefore use derivedStateOf to internally check whether navBackStackEntry?.destination?.route has update first
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf {
            navBackStackEntry?.destination?.route
        }
    }

    val topBarTitle by remember(currentRoute) {
        derivedStateOf {
            // currentRoute can be null if it is at starting home page only because no stack in navController
            if (currentRoute != null) {
                //indexOfFirst will loop through list and return index of the first item that meet the condition (it.route == currentRoute)
                bottomNavigationItemsList[bottomNavigationItemsList.indexOfFirst {
                    it.route == currentRoute
                }].title
            } else {
                bottomNavigationItemsList[0].title
            }
        }
    }
    Scaffold(
        topBar = {
            MyTopAppBar(pageTitle = topBarTitle, rootNavController = rootNavHostController, recruiterEmail="")
        },
        bottomBar = {
            BottomNavigationBar(items = bottomNavigationItemsList, currentRoute = currentRoute ){ currentNavigationItem->
                homeNavController.navigate(currentNavigationItem.route){
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    homeNavController.graph.startDestinationRoute?.let { startDestinationRoute ->
                        // Pop up to the start destination, clearing the back stack
                        popUpTo(startDestinationRoute) {
                            // Save the state of popped destinations
                            saveState = true
                        }
                    }

                    // Configure navigation to avoid multiple instances of the same destination
                    launchSingleTop = true

                    // Restore state when re-selecting a previously selected item
                    restoreState = true
                }
            }
        }
    ) {innerPadding->
        MainNavGraph(
            rootNavHostController,
            homeNavController, innerPadding,
            jobseekerViewModel, windowSize = windowSize
        )
    }
}