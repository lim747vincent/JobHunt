package com.example.jobseeker.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.jobseeker.R
import com.example.jobseeker.component.JobListingList
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel


@Composable
fun JobseekerHomeScreen(innerPadding: PaddingValues, jobseekerViewModel: JobseekerViewModel, rootNavController: NavController, windowSize:  WindowSize) {
    val context = LocalContext.current



    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    var jobPosts by remember { mutableStateOf<List<JobListingUiState>>(emptyList()) }

    LaunchedEffect(profileUiState.jobseeker_email) {
        jobseekerViewModel.setIsLoading(true)

        val fetchedJobs = jobseekerViewModel.getAllJobPost(  profileUiState.jobseeker_email        )
        jobPosts = fetchedJobs

        jobseekerViewModel.setIsLoading(false)
    }

    val isLoading by jobseekerViewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.background_purple))
            .padding(innerPadding).padding(horizontal = 16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
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
            if(jobPosts.size == 0){
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text="No results", color = colorResource(id = R.color.light_purple), fontSize = 30.sp)
                }
            }else{
                Text(
                    text = "Latest job posting",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp),
                    color = colorResource(id = R.color.light_purple)
                )

                JobListingList(jobseekerViewModel = jobseekerViewModel, context = context, rootNavController= rootNavController, list = jobPosts, windowSize = windowSize)

            }

        }


    }
}


