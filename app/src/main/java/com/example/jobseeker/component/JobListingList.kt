package com.example.jobseeker.component

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.navigations.JobDetailRouteScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.JobseekerViewModel

@Composable
fun JobListingList(jobseekerViewModel: JobseekerViewModel, modifier: Modifier = Modifier, context: Context, rootNavController: NavController, list: List<JobListingUiState>, windowSize: WindowSize) {

    when(windowSize.height){
        WindowType.Medium->{
            LazyColumn(modifier = modifier) {
                items(list) { jobListing ->
                    JobListCard(jobListingUiState = jobListing, modifier = Modifier.padding(8.dp), context = context, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize){
                        jobseekerViewModel.setJobseekerJob(jobListing)
                        rootNavController.navigate(JobDetailRouteScreen.JobDetail.passJobId(jobListing.jobpost_title))
                    }
                }
            }
        } else ->{
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),  // Set the grid to have 2 columns
            modifier = modifier
        ) {
            items(list.size) { index ->
                val jobListing = list[index]
                JobListCard(
                    jobListingUiState = jobListing,
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),  // Ensure each card takes up full width of its column
                    context = context,
                    jobseekerViewModel = jobseekerViewModel, windowSize = windowSize
                ) {
                    jobseekerViewModel.setJobseekerJob(jobListing)
                    rootNavController.navigate(JobDetailRouteScreen.JobDetail.passJobId(jobListing.jobpost_title))
                }
            }
        }
    }
    }


}