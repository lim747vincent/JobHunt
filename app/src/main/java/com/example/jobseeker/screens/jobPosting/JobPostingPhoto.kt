package com.example.jobseeker.screens.jobPosting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.jobseeker.component.CompanyCardList
import com.example.jobseeker.component.CompanySelectionCardList
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostingPhoto(
    rootNavController: NavController,
    onNextButtonClicked : () -> Unit,
    recruiterViewModel: RecruiterViewModel
){

    LaunchedEffect(Unit) {
        recruiterViewModel.fetchCompanies(recruiterViewModel.recruiterLoginEmail)
    }

    val jobUiState by recruiterViewModel.uiState_JobPost.collectAsState()
    val context = LocalContext.current
    val companies = recruiterViewModel.companies.collectAsState()

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
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Select Your Company",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.light_purple)
                ,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = Color.White, modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ){
                items(companies.value) { company ->
                    CompanySelectionCardList(company, recruiterViewModel)
                }
            }

//            // Upload Photo Button Box
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//                    .height(200.dp)
//                    .background(Color(0xFF5C5C82), shape = RoundedCornerShape(8.dp)),
//                contentAlignment = Alignment.Center
//            ) {
//                OutlinedButton(
//                    onClick = { /* Handle photo upload */ },
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .height(60.dp),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_camera),
//                        contentDescription = "Upload Photo",
//                        tint = Color.White
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Text(text = "upload_photo", color = Color.White)
//                }
//            }

            Spacer(modifier = Modifier.height(50.dp))

            // Next Button
            Button(
                onClick = {
                    if(!jobUiState.jobpost_company_name.isBlank()){
                        onNextButtonClicked()
                    }
                    else{
                        Toast.makeText(context, "Please select a company", Toast.LENGTH_SHORT).show()
                    }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF))
            ) {
                Text(text = "Next", color = colorResource(id = R.color.light_purple))
            }

        }
    }
}

@Preview
@Composable
private fun JobPostingPhotoPreview(){
    JobPostingPhoto(rootNavController = rememberNavController(), onNextButtonClicked = {}, recruiterViewModel = viewModel())
}