package com.example.jobseeker.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobseeker.R
import com.example.jobseeker.component.JobListCard
import com.example.jobseeker.component.NotificationCard
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.model.SavedJobUiState
import com.example.jobseeker.navigations.JobDetailRouteScreen
import com.example.jobseeker.navigations.NotificationRouteScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.JobseekerViewModel

/**
 * Created 28-02-2024 at 02:09 pm
 */

@Composable
fun JobseekerNotificationScreen(
    rootNavController: NavController,
    innerPadding: PaddingValues,
    jobseekerViewModel: JobseekerViewModel, windowSize: WindowSize
) {

    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    val isLoading by jobseekerViewModel.isLoading.collectAsState()

    var notifications by remember { mutableStateOf<List<JobseekerNotificationUiState>>(emptyList()) }

    LaunchedEffect(profileUiState.jobseeker_email) {
        val fetchedNotifications = jobseekerViewModel.getNotificationForJobseeker(profileUiState.jobseeker_email)
        notifications = fetchedNotifications
    }

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.background_purple))
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (isLoading) {
            // Centered CircularProgressIndicator
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp), // Change the size
                    color = colorResource(id = R.color.light_purple), // Set custom color
                    strokeWidth = 8.dp // Set custom stroke width
                )
            }
        }else{
            when(windowSize.height){
                WindowType.Medium->{
                    LazyColumn() {
                        items(notifications) { notification ->
                            NotificationCard(notification = notification, jobseekerViewModel = jobseekerViewModel){
                                jobseekerViewModel.setJobseekerNotification(notification)
                                rootNavController.navigate(NotificationRouteScreen.NotificationDetail.passNotificationId(notification.jobseeker_notification_title))
                            }
                        }
                    }
                } else ->{
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),  // Set grid to have 2 columns
                    contentPadding = PaddingValues(8.dp)  // Optional padding for grid items
                ) {
                    items(notifications.size) { index ->  // Use size and index for better flexibility
                        val notification = notifications[index]  // Access notification by index
                        NotificationCard(
                            notification = notification,
                            jobseekerViewModel = jobseekerViewModel,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            jobseekerViewModel.setJobseekerNotification(notification)
                            rootNavController.navigate(NotificationRouteScreen.NotificationDetail.passNotificationId(notification.jobseeker_notification_title))
                        }
                    }
                }
            }
            }

        }

    }
}

