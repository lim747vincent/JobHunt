package com.example.jobseeker.screens.jobDetail.recruiterProfile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.example.jobseeker.R
import com.example.jobseeker.model.CompanyUiState
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCompany(rootNavController: NavController, recruiterViewModel: RecruiterViewModel) {

    val companyUiState by recruiterViewModel.uiState_Company.collectAsState()

    val context = LocalContext.current

    var isImageUploaded by remember { mutableStateOf(false) }

    var locationExpanded by remember { mutableStateOf(false) }

    val locations = listOf("Select Option","Selangor", "Kuala Lumpur", "Pahang")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            recruiterViewModel.imageUri = it
            recruiterViewModel.uploadImageToFirebase(it) { downloadUrl ->
                if (downloadUrl.isNotEmpty()) {
                    recruiterViewModel.imageUrl = downloadUrl
                    // Optionally, set an image uploaded flag or notify user
                } else {
                    Toast.makeText(context, "Failed to retrieve image URL", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                            text = "Add Company",
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
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Fill up all company information:",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.light_purple),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Company Name Field
            OutlinedTextField(
                value = companyUiState.company_name,
                onValueChange = { recruiterViewModel.setCompanyName(it) },
                label = { Text("Company Name *", color = colorResource(id = R.color.light_purple)) },
                placeholder = { Text("Enter company name", color =colorResource(id = R.color.light_purple)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Company Description Field
            OutlinedTextField(
                value = companyUiState.company_description,
                onValueChange = { recruiterViewModel.setCompanyDescription(it) },
                label = { Text("Company description", color = colorResource(id = R.color.light_purple)) },
                placeholder = { Text("Tell us about your company...", color = colorResource(id = R.color.light_purple)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(150.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                ),
                maxLines = 5,
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Company Size Field
            OutlinedTextField(
                value = companyUiState.company_size,
                onValueChange = { recruiterViewModel.setCompanySize(it) },
                label = { Text("Company Size *", color = colorResource(id = R.color.light_purple)) },
                placeholder = { Text("In numbers", color = colorResource(id = R.color.light_purple)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = locationExpanded,
                onExpandedChange = { locationExpanded = !locationExpanded },
                modifier = Modifier
                    .padding(horizontal = 16.dp),
            ) {
                // TextField to display the selected option
                TextField(
                    value = companyUiState.company_state,
                    onValueChange = {},
                    readOnly = true, // To prevent user input
                    label = { Text("Select Location") },
                    trailingIcon = { TrailingIcon(expanded = locationExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(), // Ensures the menu is anchored correctly
                )

                // Dropdown menu
                DropdownMenu(
                    expanded = locationExpanded,
                    onDismissRequest = { locationExpanded = false }, modifier = Modifier
                        .exposedDropdownSize()
                ) {
                    locations.forEach { location ->
                        DropdownMenuItem(
                            text = { Text(text = location) },
                            onClick = {
                                recruiterViewModel.setCompanyState(location)
                                locationExpanded = false // Close the dropdown
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Address Field
            OutlinedTextField(
                value = companyUiState.company_address,
                onValueChange = { recruiterViewModel.setCompanyAddress(it) },
                label = { Text("Address", color = colorResource(id = R.color.light_purple)) },
                placeholder = { Text("Enter address", color = colorResource(id = R.color.light_purple)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Country Field
            OutlinedTextField(
                value = companyUiState.company_industry,
                onValueChange = { recruiterViewModel.setCompanyIndustry(it) },
                label = { Text("Company Industry *", color = colorResource(id = R.color.light_purple)) },
                placeholder = { Text("Your Company Industry", color = colorResource(id = R.color.light_purple)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                textStyle = TextStyle.Default.copy(
                    color = colorResource(id = R.color.light_purple)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { launcher.launch("image/*")},
                modifier = Modifier,
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.btn_color))
            ) {
                Text(text = "Pick Image", color = colorResource(id = R.color.light_purple))
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isImageUploaded) {
                recruiterViewModel.imageUri?.let {
                    Image(
                        painter = rememberImagePainter(it),
                        contentDescription = null,
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Image Uploaded: ${recruiterViewModel.imageUrl}", fontSize = 12.sp)
            }

            // Submit Button
            Button(
                onClick = {

                    if(!companyUiState.company_name.isBlank() && !companyUiState.company_industry.isBlank() && !companyUiState.company_address.isBlank() && companyUiState.company_state != "Select Location" && !companyUiState.company_size.isBlank() && !companyUiState.company_description.isBlank() && !recruiterViewModel.imageUrl.isBlank()){
                        val companyData = CompanyUiState(
                            company_name = companyUiState.company_name,
                            company_industry = companyUiState.company_industry,
                            company_address = companyUiState.company_address,
                            company_state = companyUiState.company_state,
                            company_size = companyUiState.company_size,
                            company_description = companyUiState.company_description,
                            recruiter_id = recruiterViewModel.recruiterLoginEmail,
                            company_logo_image = recruiterViewModel.imageUrl
                        )

                        recruiterViewModel.saveCompany(companyData, context)

                        recruiterViewModel.setCompanyName("")
                        recruiterViewModel.setCompanySize("")
                        recruiterViewModel.setCompanyIndustry("")
                        recruiterViewModel.setCompanyAddress("")
                        recruiterViewModel.setCompanyDescription("")
                        recruiterViewModel.setCompanyLogoImage("")
                        recruiterViewModel.setCompanyState("")
                    }
                    else{
                        Toast.makeText(context, "Please ensure that all fields has been inserted correctly.", Toast.LENGTH_SHORT).show()
                    }
                          },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF))
            ) {
                Text(text = "Submit", color = colorResource(id = R.color.light_purple))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCompanyPreview() {
    AddCompany(rootNavController = rememberNavController(), recruiterViewModel = viewModel())
}