package com.example.jobseeker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import com.example.jobseeker.model.NavigationItem
import com.example.jobseeker.navigations.MainRouteScreen

val bottomNavigationItemsList = listOf(
    NavigationItem(
        title = "Home",
        route = MainRouteScreen.Home.route,
        selectedIcon = Icons.Filled.Home,
        unSelectedIcon = Icons.Outlined.Home,
    ),
    NavigationItem(
        title = "Activity",
        route = MainRouteScreen.Activity.route,
        selectedIcon = Icons.Filled.Mail,
        unSelectedIcon = Icons.Outlined.Mail,
    ),
    NavigationItem(
        title = "Notification",
        route = MainRouteScreen.Notification.route,
        selectedIcon = Icons.Filled.Notifications,
        unSelectedIcon = Icons.Outlined.Notifications,
        badgeCount = 9
    ),
    NavigationItem(
        title = "Profile",
        route = MainRouteScreen.Profile.route,
        selectedIcon = Icons.Filled.Person,
        unSelectedIcon = Icons.Outlined.Person
    ),
)