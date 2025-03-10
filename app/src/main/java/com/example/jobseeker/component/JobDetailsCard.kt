package com.example.jobseeker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.jobseeker.R
import com.example.jobseeker.viewModel.RecruiterViewModel


@Composable
fun JobDetailsCard(recruiterViewModel: RecruiterViewModel) {

    val jobUiState by recruiterViewModel.uiState_JobPost.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.light_purple)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = jobUiState.jobpost_title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = jobUiState.jobpost_company_name,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = rememberImagePainter(jobUiState.jobpost_company_logo_image_filepath),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
            )

            JobDetailRow("JobPost_Description", jobUiState.jobpost_description)
            JobDetailRow("Company_Industry", jobUiState.jobpost_company_industry)
            JobDetailRow("Company_Size", jobUiState.jobpost_company_size)
            JobDetailRow("Salary", "RM ${jobUiState.jobpost_salary_start} - RM ${jobUiState.jobpost_salary_end}")

            Spacer(modifier = Modifier.height(16.dp))

            IconAndText(
                iconId = R.drawable.baseline_location,
                text = "${jobUiState.jobpost_company_address},\n${jobUiState.jobpost_company_state}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            IconAndText(
                iconId = R.drawable.baseline_account_circle,
                text = "Hiring by ${jobUiState.recruiter_id}"
            )
        }
    }
}

@Composable
fun JobDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun IconAndText(iconId: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}
