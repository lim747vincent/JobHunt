package com.example.jobseeker.component

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.jobseeker.R
import com.example.jobseeker.model.CompanyUiState
import com.example.jobseeker.model.RecruiterUiState
import com.example.jobseeker.viewModel.RecruiterViewModel

@Composable
fun CompanyCard(
    companyName: String,
    createdDate: String,
    companyImage: String,
    companySize: String,
    companyIndustry: String,
    companyState: String,
    companyAddress: String,
    onRemoveClick: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4D4C73) // Match background color in the image
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = createdDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB5B5D2)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ){
                Image(
                    painter = rememberImagePainter(companyImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Company Size: $companySize",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Company Industry: $companyIndustry",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Company State: $companyState",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Company Address: $companyAddress",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()

            ) {
                Button(
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBB4A4A)),
                    modifier = Modifier.padding(horizontal = 2.dp)
                ) {
                    Text(text = "Remove", color = colorResource(id = R.color.light_purple))
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "",
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showDialog = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Dialog"
                        )
                    }
                }
            },
            text = {
                Text(
                    text = "Are you sure want to remove it?",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF))
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            showDialog = false
                            onRemoveClick() // Trigger the removal logic
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF))
                    ) {
                        Text("Yes")
                    }
                }
            }
        )
    }
}

@Composable
fun CompanySelectionCard(
    companyName: String,
    createdDate: String,
    companyImage: String,
    onSelectClick : () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4D4C73) // Match background color in the image
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = companyName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorResource(id = R.color.light_purple),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = createdDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFB5B5D2)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ){
                Image(
                    painter = rememberImagePainter(companyImage),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()

            ) {
                Button(
                    onClick = { onSelectClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF)),
                    modifier = Modifier.padding(horizontal = 2.dp)
                ) {
                    Text(text = "Select", color= colorResource(id = R.color.light_purple))
                }
            }
        }
    }
}

@Composable
fun CompanyCardList(companies : CompanyUiState, recruiterViewModel: RecruiterViewModel) {
    
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CompanyCard(
            companyName = companies.company_name,
            createdDate = companies.company_description,
            companyImage = companies.company_logo_image,
            companySize = companies.company_size,
            companyIndustry = companies.company_industry,
            companyState = companies.company_state,
            companyAddress = companies.company_address,
            onRemoveClick = { recruiterViewModel.deleteCompanyByName(companies.company_name, context)}
        )
    }
}

@Composable
fun CompanySelectionCardList(companies : CompanyUiState, recruiterViewModel: RecruiterViewModel) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CompanySelectionCard(
            companyName = companies.company_name,
            createdDate = companies.company_description,
            companyImage = companies.company_logo_image,
            onSelectClick = {
                Toast.makeText(context, "You have selected ${companies.company_name}", Toast.LENGTH_SHORT).show()

                recruiterViewModel.setJobPostCompanyName(companies.company_name)
                recruiterViewModel.setJobPostCompanyState(companies.company_state)
                recruiterViewModel.setJobPostCompanyAddress(companies.company_address)
                recruiterViewModel.setJobPostCompanyLogoImage(companies.company_logo_image)
                recruiterViewModel.setJobPostCompanyIndustry(companies.company_industry)
                recruiterViewModel.setJobPostCompanySize(companies.company_size)
                recruiterViewModel.setJobPostRecruiterId(recruiterViewModel.getRecruiterEmail)
            }
        )
    }
}

@Preview
@Composable
fun CompanyCardPreview() {
    CompanyCardList(companies = CompanyUiState(), recruiterViewModel = viewModel())
}