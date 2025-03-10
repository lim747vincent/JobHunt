package com.example.jobseeker.screens.jobPosting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.viewModel.RecruiterViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostingSuccessful(rootNavController: NavController, recruiterViewModel:RecruiterViewModel){

    val jobUiState by recruiterViewModel.uiState_JobPost.collectAsState()

    LaunchedEffect(Unit){
        delay(3000)
        rootNavController.navigate(Graph.RecruiterMainScreenGraph){
            popUpTo(Graph.JobPostingSuccessfulGraph){ inclusive = true }
        }
        recruiterViewModel.getRecruiterCreditScore = recruiterViewModel.getCreditScoreAmount(recruiterViewModel.recruiterLoginEmail)
            .toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.top_bar_color)),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    ) {
                        Text(
                            text = "Add New Job Posting",
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.background_purple))
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Job title and company
            Text(
                text = jobUiState.jobpost_title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.light_purple)
            )
            Text(
                text = jobUiState.jobpost_company_name,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.light_purple)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Success Icon
            Icon(
                painter = painterResource(id = R.drawable.successfuladd),  // Replace with the correct icon resource
                contentDescription = "Success Icon",
                tint = Color.Green,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Success Message
            Text(
                text = "Your job is posted successfully!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Green,
                textAlign = TextAlign.Center
            )
        }

        }
    }


@Preview
@Composable
private fun JobPostingSuccessfulPreview(){
    JobPostingSuccessful(rootNavController = rememberNavController(), recruiterViewModel = viewModel())
}