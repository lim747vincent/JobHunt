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
import com.example.jobseeker.component.RecruiterNotificationCard
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.model.RecruiterNotificationUiState
import com.example.jobseeker.model.SavedJobUiState
import com.example.jobseeker.navigations.JobDetailRouteScreen
import com.example.jobseeker.navigations.RecruiterNotificationRouteScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

/**
 * Created 28-02-2024 at 02:09 pm
 */

@Composable
fun RecruiterNotificationScreen(
    rootNavController: NavController,
    innerPadding: PaddingValues,
    recruiterViewModel: RecruiterViewModel, windowSize: WindowSize
) {

    //val profileUiState by recruiterViewModel.uiState_Profile.collectAsState()

    val isLoading by recruiterViewModel.isLoading.collectAsState()

    var notifications by remember { mutableStateOf<List<RecruiterNotificationUiState>>(emptyList()) }

    LaunchedEffect(recruiterViewModel.getRecruiterEmail) {
        val fetchedNotifications = recruiterViewModel.getNotificationForRecruiter(recruiterViewModel.getRecruiterEmail)
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
                            RecruiterNotificationCard(notification = notification, recruiterViewModel = recruiterViewModel){
                                recruiterViewModel.setRecruiterNotification(notification)
                                rootNavController.navigate(RecruiterNotificationRouteScreen.RecruiterNotificationDetail.passNotificationId(notification.recruiter_notification_title))
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
                        RecruiterNotificationCard(
                            notification = notification,
                            recruiterViewModel = recruiterViewModel,
                            modifier = Modifier
                                .padding(8.dp)
                        ) {
                            recruiterViewModel.setRecruiterNotification(notification)
                            rootNavController.navigate(RecruiterNotificationRouteScreen.RecruiterNotificationDetail.passNotificationId(notification.recruiter_notification_title))
                        }
                    }
                }
            }
            }

        }

    }
}