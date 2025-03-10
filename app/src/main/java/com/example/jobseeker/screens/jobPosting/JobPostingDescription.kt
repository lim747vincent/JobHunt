package com.example.jobseeker.screens.jobPosting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostingDescription(
    rootNavController: NavController,
    onNextButtonClicked : () -> Unit,
    recruiterViewModel: RecruiterViewModel
){

    val jobUiState by recruiterViewModel.uiState_JobPost.collectAsState()
    val context = LocalContext.current

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
                            text = "Job Posting Module",
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
        Column (
            modifier = Modifier
                .background(colorResource(id = R.color.background_purple))
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize().verticalScroll(rememberScrollState())){

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Job Description",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.light_purple),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Divider(color = Color.White, modifier = Modifier.padding(16.dp))

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Job position you are hiring *",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = colorResource(id = R.color.light_purple),
                modifier = Modifier.padding(horizontal = 16.dp)
            )


            OutlinedTextField(
                value = jobUiState.jobpost_title,
                onValueChange = { recruiterViewModel.setJobPostTitle(it) },
                placeholder = { Text("Enter job position", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Description *",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = colorResource(id = R.color.light_purple),
                modifier = Modifier.padding(horizontal = 16.dp)
            )


            OutlinedTextField(
                value = jobUiState.jobpost_description,
                onValueChange = { recruiterViewModel.setJobPostCompanyDescription(it) },
                placeholder = { Text("Describe the role clearly to attract quality candidates", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(150.dp),
                shape = RoundedCornerShape(8.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if(!jobUiState.jobpost_title.isBlank() && !jobUiState.jobpost_description.isBlank()){
                        onNextButtonClicked()
                    }else{
                        Toast.makeText(context, "Please ensure that all fields has been entered.", Toast.LENGTH_SHORT).show()
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
private fun JobPostingDescriptionPreview(){
    JobPostingDescription(rootNavController = rememberNavController(), onNextButtonClicked = {}, recruiterViewModel = viewModel())
}