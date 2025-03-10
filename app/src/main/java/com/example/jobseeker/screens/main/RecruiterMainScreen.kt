package com.example.jobseeker.screens.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.jobseeker.model.NavigationItem
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.navigations.graphs.MainNavGraph
import com.example.jobseeker.navigations.graphs.RecruiterMainNavGraph
import com.example.jobseeker.utils.RecuiterBottomNavigationItemsList
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.bottomNavigationItemsList
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

@Composable
fun RecruiterMainScreen(
    rootNavHostController: NavHostController,
    homeNavController : NavHostController = rememberNavController(),
    jobseekerViewModel: JobseekerViewModel,
    recruiterViewModel: RecruiterViewModel,
    windowSize: WindowSize
){
    var badgeCount by remember { mutableStateOf(0) }

    LaunchedEffect(recruiterViewModel.getRecruiterEmail) {
        val fetchedNotifications = recruiterViewModel.getNotificationForRecruiter(recruiterViewModel.getRecruiterEmail)
        badgeCount = fetchedNotifications.count { !it.recruiter_notification_isRead }
    }

    val RecuiterBottomNavigationItemsList = listOf(
        NavigationItem(
            title = "Home",
            route = MainRouteScreen.recruiterHome.route,
            selectedIcon = Icons.Default.Home,
            unSelectedIcon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = "Notification",
            route = MainRouteScreen.recruiterNotification.route,
            selectedIcon = Icons.Default.Notifications,
            unSelectedIcon = Icons.Outlined.Notifications,
            badgeCount = badgeCount
        ),
        NavigationItem(
            title = "Profile",
            route = MainRouteScreen.recruiterProfile.route,
            selectedIcon = Icons.Default.AccountCircle,
            unSelectedIcon = Icons.Outlined.AccountCircle
        )
    )

    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()

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
                RecuiterBottomNavigationItemsList[RecuiterBottomNavigationItemsList.indexOfFirst {
                    it.route == currentRoute
                }].title
            } else {
                RecuiterBottomNavigationItemsList[0].title
            }
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(pageTitle = topBarTitle, rootNavController = rootNavHostController, recruiterEmail = recruiterViewModel.getRecruiterEmail)
        },
        bottomBar = {
            BottomNavigationBar(items = RecuiterBottomNavigationItemsList, currentRoute = currentRoute ){ currentNavigationItem->
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
        RecruiterMainNavGraph(
            rootNavHostController,
            homeNavController, innerPadding,
            jobseekerViewModel,
            recruiterViewModel,
            windowSize
        )
    }
}