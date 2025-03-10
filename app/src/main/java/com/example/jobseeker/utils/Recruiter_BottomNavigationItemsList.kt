package com.example.jobseeker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import com.example.jobseeker.model.NavigationItem
import com.example.jobseeker.navigations.MainRouteScreen

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
        badgeCount = 1
    ),
    NavigationItem(
        title = "Profile",
        route = MainRouteScreen.recruiterProfile.route,
        selectedIcon = Icons.Default.AccountCircle,
        unSelectedIcon = Icons.Outlined.AccountCircle
        )
)