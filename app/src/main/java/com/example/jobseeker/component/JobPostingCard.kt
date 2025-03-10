package com.example.jobseeker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.jobseeker.R
import com.example.jobseeker.model.JobPostingUiState
import com.example.jobseeker.model.RecruiterUiState
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.viewModel.RecruiterViewModel

@Composable
fun JobPostingCard(
    activeJobPost : JobPostingUiState,
    recruiterViewModel: RecruiterViewModel
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3C3C70)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title and Basic Information
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = activeJobPost.jobpost_title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activeJobPost.jobpost_company_name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_work_24),
                            contentDescription = "Location Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeJobPost.jobpost_employment_type,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(
                            painter = painterResource(R.drawable.baseline_location_pin_24),
                            contentDescription = "Location Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = activeJobPost.jobpost_company_state,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Icon(
                            painter = painterResource(R.drawable.baseline_money),
                            contentDescription = "Salary Icon",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RM ${activeJobPost.jobpost_salary_start} - RM ${activeJobPost.jobpost_salary_end}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = "Company Industry: ${activeJobPost.jobpost_company_industry}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = "Company State: ${activeJobPost.jobpost_company_state}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = "Company Address: ${activeJobPost.jobpost_company_address}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

        }
    }
}


@Preview
@Composable
fun JobPostingCardPreview() {
    JobPostingCard(
        activeJobPost = JobPostingUiState(),
        recruiterViewModel = viewModel()
    )
}