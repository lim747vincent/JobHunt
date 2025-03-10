package com.example.jobseeker.screens.jobDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.component.JobPostingCard
import com.example.jobseeker.component.JobPostingClosedCard
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostingHistory(rootNavController: NavController, recruiterViewModel: RecruiterViewModel) {

    LaunchedEffect(Unit) {
        recruiterViewModel.fetchActiveJobPost(recruiterViewModel.recruiterLoginEmail)
        recruiterViewModel.fetchInActiveJobPost(recruiterViewModel.recruiterLoginEmail)
    }

    val activeJobPost = recruiterViewModel.activeJobPost.collectAsState()

    val inactiveJobPost = recruiterViewModel.inactiveJobPost.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id =R.color.top_bar_color)),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    ) {
                        Text(
                            text = "Job Posting History",
                            color = colorResource(id = R.color.page_title_color),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        rootNavController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { padding ->
        var selectedTabIndex by remember { mutableStateOf(0) }

        Column(modifier = Modifier.background(colorResource(id = R.color.background_purple)).padding(padding)) {
            // Tabs for Active and Closed Job Postings
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = colorResource(id = R.color.tab_color)
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Active") }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Inactive") }
                )
            }

            // Content based on selected tab
            if (selectedTabIndex == 0) {
                // Active job postings
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(activeJobPost.value) { active_job_post ->
                        JobPostingCard(
                            active_job_post, recruiterViewModel = recruiterViewModel
                        )
                    }
                }
            } else {
                // Closed job postings
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(inactiveJobPost.value) { inactive_job_post ->
                        JobPostingClosedCard(
                            inactive_job_post, recruiterViewModel
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun JobPostingHistoryPreview() {
    JobPostingHistory(rootNavController = rememberNavController(), recruiterViewModel = viewModel())
}
