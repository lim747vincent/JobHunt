package com.example.jobseeker.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobseeker.model.JobApplicationUiState
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.RecruiterViewModel


@Composable
fun ApplicationCard(
    jobApplicationId : String,
    name: String,
    jobId: String,
    status: String,
    onViewClick : (String) -> Unit,
    windowSize: WindowSize
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFF3F3F72), shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ) {
        when(windowSize.height){
            WindowType.Medium->{
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jobseeker ID: $name",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            ),
                            color = Color.White
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            text = "Job Application Id: $jobApplicationId",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Job Details and Status
                    Text(
                        text = "Job Posting ID: $jobId\nApplication Status: $status",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // View Button Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = { onViewClick(jobApplicationId) },
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "View", color = Color.White)
                        }
                    }
                }
            } else ->{
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // Add padding around the content
                    horizontalAlignment = Alignment.CenterHorizontally, // Center content horizontally
                    verticalArrangement = Arrangement.Center // Center content vertically
                ) {
                    // Name Row - centered
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.White
                    )

                    // Spacer for spacing between name and jobApplicationId
                    Spacer(modifier = Modifier.height(8.dp))

                    // Job Application ID Row - centered
                    Text(
                        text = jobApplicationId,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Job Details and Status - centered
                    Text(
                        text = "Job Posting ID: $jobId\nApplication Status: $status",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textAlign = TextAlign.Center // Center align the text inside the Text component
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // View Button Row - centered
                    Button(
                        onClick = { onViewClick(jobApplicationId) },
                        modifier = Modifier
                            .fillMaxWidth(0.5f) // Reduce button width to half of the screen width
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "View", color = Color.White)
                    }
                }
            }
        }














    }
}

@Composable
fun ApplicationCardList(
    jobApplication:JobApplicationUiState,
    recruiterViewModel: RecruiterViewModel,
    onViewClick: (String) -> Unit,
    windowSize: WindowSize
){
    Column {
        ApplicationCard(
            jobApplicationId = jobApplication.jobApplication_id,
            jobId = jobApplication.jobpost_id,
            name = jobApplication.jobseeker_id,
            status = jobApplication.jobApplication_status,
            onViewClick = onViewClick,
            windowSize = windowSize
            )
    }
}

//@Preview
//@Composable
//fun ApplicationCardPreview() {
//    ApplicationCardList(jobApplication = JobApplicationUiState(), recruiterViewModel = viewModel(), onViewClick = {})
//}