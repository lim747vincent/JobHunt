package com.example.jobseeker.screens.jobDetail

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.JobDetailRouteScreen
import com.example.jobseeker.viewModel.JobseekerViewModel
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.text.DecimalFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobFilteringScreen(
    navController: NavController, jobseekerViewModel: JobseekerViewModel
) {
    val jobFilteringUiState by jobseekerViewModel.uiState_JobFilteringForm.collectAsState()

    var employmentExpanded by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }

    val employmentTypes = listOf("Select Option","Full-time", "Part-time")
    val locations = listOf("Select Option","Selangor", "Kuala Lumpur", "Pahang")

    val decimalFormat = DecimalFormat("#.##")




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
                            text = "Job Filtering Screen",
                            color = colorResource(id = R.color.page_title_color),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) {innerPadding->

        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.background_purple))
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 30.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            TextField(
                value = jobFilteringUiState.jobTitle,
                onValueChange = { jobseekerViewModel.updateJobTitle(it) },
                label = { Text("Search Job Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))
            // spinner
            ExposedDropdownMenuBox(
                expanded = employmentExpanded,
                onExpandedChange = { employmentExpanded = !employmentExpanded }
            ) {
                // TextField to display the selected option
                TextField(
                    value = jobFilteringUiState.selectedEmploymentType,
                    onValueChange = {},
                    readOnly = true, // To prevent user input
                    label = { Text("Select Employment Type") },
                    trailingIcon = { TrailingIcon(expanded = employmentExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // Ensures the menu is anchored correctly
                )

                // Dropdown menu
                DropdownMenu(
                    expanded = employmentExpanded,
                    onDismissRequest = { employmentExpanded = false }, modifier = Modifier
                        .exposedDropdownSize()
                ) {
                    employmentTypes.forEach { employmentType ->
                        DropdownMenuItem(
                            text = { Text(text = employmentType) },
                            onClick = {
                                jobseekerViewModel.updateSelectedEmploymentType(employmentType)
                                employmentExpanded = false // Close the dropdown
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            ExposedDropdownMenuBox(
                expanded = locationExpanded,
                onExpandedChange = { locationExpanded = !locationExpanded }
            ) {
                // TextField to display the selected option
                TextField(
                    value = jobFilteringUiState.selectedLocation,
                    onValueChange = {},
                    readOnly = true, // To prevent user input
                    label = { Text("Select Location") },
                    trailingIcon = { TrailingIcon(expanded = locationExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor() // Ensures the menu is anchored correctly
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
                                jobseekerViewModel.updateSelectedLocation(location)
                                locationExpanded = false // Close the dropdown
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Range Slider for Salary
            Text(
                text = "Salary Range: RM ${decimalFormat.format(jobFilteringUiState.salaryRange.start)} - RM ${decimalFormat.format(jobFilteringUiState.salaryRange.endInclusive)}",
                color = colorResource(id = R.color.light_purple)
            )

            RangeSlider(
                value = jobFilteringUiState.salaryRange,
                onValueChange = { jobseekerViewModel.updateSalaryRange(it) },
                valueRange = 0f..10000f,
                onValueChangeFinished = {
                    // ex: viewModel.updateSoundVolume(soundVolume)

                },
                steps = 1000,
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                navController.navigate(JobDetailRouteScreen.FilteringResult.route)
            },
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    containerColor = colorResource(id = R.color.light_blue)
                ),
                shape = RoundedCornerShape(25),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally) // Centers the image inside the Box
                    .fillMaxWidth() // Adjust as necessary, or set a specific width/height
                    .height(70.dp) // Set the height for the image if you want
            ) {
                Text(
                    text="SEARCH JOB",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

        }

    }


}



@Preview(showBackground = true)
@Composable
private fun JobFilteringScreenPreview() {
    JobFilteringScreen(jobseekerViewModel = JobseekerViewModel(), navController = rememberNavController())
}