package com.example.jobseeker.screens.main

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.jobseeker.component.JobListCard
import com.example.jobseeker.component.JobListingList
import com.example.jobseeker.model.JobApplicationUiState
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.model.SavedJobUiState
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created 28-02-2024 at 02:09 pm
 */

@Composable
fun JobseekerActivityScreen(
    innerPadding: PaddingValues,
    jobseekerViewModel: JobseekerViewModel,
    rootNavController: NavController, windowSize: WindowSize
) {
    val context = LocalContext.current



    Column(
        modifier = Modifier
            .background(colorResource(id = R.color.background_purple))
            .padding(innerPadding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState (
            pageCount = {2}
        )

        val coroutineScope = rememberCoroutineScope()

        TabRow(selectedTabIndex = pagerState.currentPage,
            containerColor = colorResource(id = R.color.tab_color),
            divider = {},
            indicator = {tabPositions -> TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                height = 4.dp,
                color = colorResource(id = R.color.indicator_color)
            ) }
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                text = { Text(text = "Saved Job") },
                modifier = Modifier.fillMaxWidth()
            )
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                },
                text = { Text(text = "Applied Job") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        HorizontalPager(state = pagerState, userScrollEnabled = false) {
                page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ){
                when(page){
                    0-> SavedJob(context = context, jobseekerViewModel = jobseekerViewModel, rootNavController = rootNavController, windowSize = windowSize)
                    1-> AppliedJob(context = context, jobseekerViewModel = jobseekerViewModel, rootNavController = rootNavController, windowSize = windowSize)
                }
            }
        }

    }
}




@Composable
fun SavedJob(context: Context, jobseekerViewModel: JobseekerViewModel, rootNavController: NavController, windowSize: WindowSize){

    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    val isLoading by jobseekerViewModel.isLoading.collectAsState()

    var savedJobsWithDetails by remember { mutableStateOf<List<Pair<SavedJobUiState, JobListingUiState>>>(emptyList()) }

    LaunchedEffect(profileUiState.jobseeker_email) {
        val fetchedSavedJobsWithDetails = jobseekerViewModel.getSavedJobsForJobseeker(profileUiState.jobseeker_email)
        savedJobsWithDetails = fetchedSavedJobsWithDetails
    }

    // Update UI state or list that you will pass to LazyColumn
    val jobListingUiStateList = savedJobsWithDetails.map { (savedJob, jobPost) ->
        JobListingUiState(
            jobPost_id= jobPost.jobPost_id,
            jobpost_title = jobPost.jobpost_title,
            jobpost_company_name = jobPost.jobpost_company_name,
            jobpost_company_logo_image_filepath = jobPost.jobpost_company_logo_image_filepath,
            jobpost_company_address = jobPost.jobpost_company_address,
            jobpost_company_state = jobPost.jobpost_company_state,
            jobpost_description = jobPost.jobpost_description,
            jobpost_employment_type = jobPost.jobpost_employment_type,
            jobpost_end_date = jobPost.jobpost_end_date,
            jobpost_salary_end = jobPost.jobpost_salary_end,
            jobpost_salary_start = jobPost.jobpost_salary_start,
            jobpost_start_date = jobPost.jobpost_start_date,
            jobpost_status = jobPost.jobpost_status,
            recruiter_id = jobPost.recruiter_id,
            isFavor =  true,
            jobApplication_status = jobPost.jobApplication_status
        )
    }

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
        if(savedJobsWithDetails.size == 0){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text="No results", color = colorResource(id = R.color.light_purple), fontSize = 30.sp)
            }
        }else{
            JobListingList(jobseekerViewModel = jobseekerViewModel, context = context, rootNavController = rootNavController, list = jobListingUiStateList, windowSize=windowSize)

        }

    }



}

@Composable
fun AppliedJob(context:Context, jobseekerViewModel: JobseekerViewModel, rootNavController: NavController, windowSize: WindowSize){

    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    val isLoading by jobseekerViewModel.isLoading.collectAsState()

    var appliedJobsWithDetails by remember { mutableStateOf<List<Pair<JobApplicationUiState, JobListingUiState>>>(emptyList()) }

    LaunchedEffect(profileUiState.jobseeker_email) {
        val fetchedJobsWithDetails = jobseekerViewModel.getAppliedJobsForJobseeker(profileUiState.jobseeker_email)
        appliedJobsWithDetails = fetchedJobsWithDetails
    }

    // Update UI state or list that you will pass to LazyColumn
    val jobListingUiStateList = appliedJobsWithDetails.map { (appliedJob, jobPost) ->
        JobListingUiState(
            jobPost_id= jobPost.jobPost_id,
            jobpost_title = jobPost.jobpost_title,
            jobpost_company_name = jobPost.jobpost_company_name,
            jobpost_company_logo_image_filepath = jobPost.jobpost_company_logo_image_filepath,
            jobpost_company_address = jobPost.jobpost_company_address,
            jobpost_company_state = jobPost.jobpost_company_state,
            jobpost_description = jobPost.jobpost_description,
            jobpost_employment_type = jobPost.jobpost_employment_type,
            jobpost_end_date = jobPost.jobpost_end_date,
            jobpost_salary_end = jobPost.jobpost_salary_end,
            jobpost_salary_start = jobPost.jobpost_salary_start,
            jobpost_start_date = jobPost.jobpost_start_date,
            jobpost_status = jobPost.jobpost_status,
            recruiter_id = jobPost.recruiter_id,
            isFavor =  jobPost.isFavor,
            jobApplication_status = appliedJob.jobApplication_status
        )
    }

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
        if(appliedJobsWithDetails.size == 0){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text="No results", color = colorResource(id = R.color.light_purple), fontSize = 30.sp)
            }
        }else{
            JobListingList(jobseekerViewModel = jobseekerViewModel, context = context, rootNavController=rootNavController, list = jobListingUiStateList, windowSize = windowSize)

        }

    }


}