package com.example.jobseeker.screens.applicant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.component.ApplicationCardList
import com.example.jobseeker.model.JobApplicationUiState
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationModule(rootNavController: NavController, recruiterViewModel: RecruiterViewModel, windowSize:WindowSize){

    LaunchedEffect(Unit) {
        recruiterViewModel.fetchJobApplicationsByRecruiter(recruiterViewModel.recruiterLoginEmail)
    }

    val jobApplication = recruiterViewModel.jobApplications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.top_bar_color)),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp), // Fill the width of the TopAppBar
                    ) {
                        Text(
                            text = "Application Module",
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
    ){innerPadding->
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.background_purple))
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){


            Text(
                text = "All job posting: ",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                modifier = Modifier
            ){
                items(jobApplication.value) { job_application ->
                    ApplicationCardList(
                        job_application,
                        recruiterViewModel,
                        onViewClick = { jobApplicationId ->
                            rootNavController.navigate("${Graph.ApplicantDetailsGraph}/$jobApplicationId")
                        },
                        windowSize
                        )
                }
            }


            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


//@Preview
//@Composable
//private fun ApplicationModulePreview(){
//    ApplicationModule(rootNavController = rememberNavController(), recruiterViewModel = viewModel())
//}