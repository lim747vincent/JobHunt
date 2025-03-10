package com.example.jobseeker.screens.jobDetail

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.load
import coil.size.Scale
import com.example.jobseeker.R
import com.example.jobseeker.component.JobListCard
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.JobDetailRouteScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.JobseekerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    navController: NavController, jobseekerViewModel: JobseekerViewModel, windowSize: WindowSize
) {
    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    val jobUiState by jobseekerViewModel.uiState_SelectedJob.collectAsState()

    var showApplyJobDialog by remember { mutableStateOf(false) }

    var showNeedResumeDialog by remember { mutableStateOf(false) }

    val resumeUiState by jobseekerViewModel.uiState_Resume.collectAsState()

//    var expanded by remember { mutableStateOf(false) }

//    val selectedResume by jobseekerViewModel.selectedResume.collectAsState()

//    val resumeOptions = listOf("Resume 1", "Resume 2", "Resume 3")

//    var showAlert by remember { mutableStateOf(false) }

//    fun checkCondition(): Boolean {
//
//        if(selectedResume == "Select Resume" || selectedResume.isNullOrEmpty()){
//            return false
//        }else{
//            return true
//        }
//    }

    val imageHeightPx = with(LocalDensity.current) { 100.dp.toPx().toInt() }
    val imageWidthPx = with(LocalDensity.current) { 100.dp.toPx().toInt() }



    if (showNeedResumeDialog) {
        AlertDialog(
            onDismissRequest = {
                showNeedResumeDialog = false // Dismiss dialog when clicked outside
            },
            title = {
                Text(text = "Alert")
            },
            text = {
                Text("You need to have uploaded resume to proceed action.")
            },
            confirmButton = {
                // Wrapped confirm and dismiss buttons in a Row for alignment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Dismiss Button
                    Button(
                        onClick = {
                            showNeedResumeDialog = false // Just close the dialog if "No" is clicked
                        }
                    ) {
                        Text("Ok")
                    }
                }
            }

        )
    }

    if (showApplyJobDialog) {
        AlertDialog(
            onDismissRequest = {
                showApplyJobDialog = false // Dismiss dialog when clicked outside
            },
            title = {
                Text(text = "Job Application Confirmation")
            },
            text = {
                Text("Are you sure you want to used your uploaded resume to apply this job?\nAfter apply, you can't undo action.")
            },
            confirmButton = {
                // Wrapped confirm and dismiss buttons in a Row for alignment
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Dismiss Button
                    Button(
                        onClick = {
                            showApplyJobDialog = false // Just close the dialog if "No" is clicked
                        }
                    ) {
                        Text("No")
                    }

                    // Confirm Button
                    Button(
                        onClick = {
                            showApplyJobDialog = false // Dismiss dialog

                            CoroutineScope(Dispatchers.Main).launch {
                                jobseekerViewModel.addAppliedJob(jobseekerId = profileUiState.jobseeker_email, jobpostId = jobUiState.jobPost_id)
                                jobseekerViewModel.sendRecruiterNotification(jobPostId = jobUiState.jobPost_id, jobPostTitle = "New Applicant!", jobseekerId = profileUiState.jobseeker_email, recruiterId = jobUiState.recruiter_id)
                            }

                            navController.navigate(JobDetailRouteScreen.ApplySuccess.route)

                        }
                    ) {
                        Text("Yes")
                    }
                }
            }

        )
    }

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
                            text = "Job Detail Screen",
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

        if(jobUiState != null){


            when(windowSize.height){
                WindowType.Medium->{
                    Column(
                        modifier = Modifier
                            .background(colorResource(id = R.color.background_purple))
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 30.dp)
                            .fillMaxSize().verticalScroll(rememberScrollState())
                        ,
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {



                        AndroidView(factory = { context ->
                            ImageView(context).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    imageWidthPx,
                                    imageHeightPx
                                )

                                // Set the image loading behavior
                                load(jobUiState.jobpost_company_logo_image_filepath) {
                                    scale(Scale.FIT)
                                }
                            }
                        },
                            modifier = Modifier
                                .height(100.dp) // Fixed height
                                .fillMaxWidth(1f)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = jobUiState.jobpost_title,
                            fontSize = 32.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = jobUiState.jobpost_company_name,
                            fontSize = 20.sp,
                            color = colorResource(id = R.color.light_purple)
                        )



                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()  // Make the line span the full width
                                .padding(vertical = 18.dp),  // Optional padding
                            thickness = 1.dp,   // Define the thickness of the line
                            color = Color.Gray // Set the color to grey
                        )

                        Text(
                            text = "Job Post id: ${jobUiState.jobPost_id}",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple),
                        )

                        // if user has apply this job then show the progress status
                        Text(
                            text = "Job Status: ${jobUiState.jobpost_status}",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple),
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Work Type: "+ jobUiState.jobpost_employment_type,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = "Location: " + jobUiState.jobpost_company_address,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = "Salary: RM ${jobUiState.jobpost_salary_start} - RM ${jobUiState.jobpost_salary_end}",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = "Posted Date: ${jobUiState.jobpost_start_date}",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = "Recruiter Id: ${jobUiState.recruiter_id}",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = "Jobpost status: " + jobUiState.jobpost_status,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        if(jobUiState.jobApplication_status != "Not Applied"){
                            Text(
                                text = "Application status: " +jobUiState.jobApplication_status,
                                fontSize = 14.sp,
                                color = if(jobUiState.jobApplication_status == "Approved"){
                                    Color.Green
                                }else if(jobUiState.jobApplication_status == "Rejected"){
                                    Color.Red
                                }else{
                                    Color.Yellow
                                }
                                ,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 12.sp
                            )
                        }


                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()  // Make the line span the full width
                                .padding(vertical = 12.dp),  // Optional padding
                            thickness = 1.dp,   // Define the thickness of the line
                            color = Color.Gray // Set the color to grey
                        )

                        Text(
                            text = "Descriptions: \n${jobUiState.jobpost_description}",
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        if(jobUiState.jobpost_status != "Inactive"){

                            if(jobUiState.jobApplication_status == "Not Applied"){
                                Button(onClick = {


                                    if(!resumeUiState.uploaded_resume_file_name.isNullOrEmpty()){
                                        showApplyJobDialog = true

                                    }else{
                                        showNeedResumeDialog = true
                                    }


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
                                        text="APPLY NOW",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }
                    }



                } else ->{
                Column(
                    modifier = Modifier
                        .background(colorResource(id = R.color.background_purple))
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp, vertical = 30.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally // Center all elements horizontally
                ) {
                    AndroidView(factory = { context ->
                        ImageView(context).apply {
                            layoutParams = LinearLayout.LayoutParams(
                                imageWidthPx,
                                imageHeightPx
                            )
                            // Set the image loading behavior
                            load(jobUiState.jobpost_company_logo_image_filepath) {
                                scale(Scale.FIT)
                            }
                        }
                    },
                        modifier = Modifier
                            .height(100.dp) // Fixed height
                            .fillMaxWidth(1f) // Ensures the image fills the width of its container
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = jobUiState.jobpost_title,
                        fontSize = 32.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center title
                    )

                    Text(
                        text = jobUiState.jobpost_company_name,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center company name
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()  // Make the line span the full width
                            .padding(vertical = 18.dp),  // Optional padding
                        thickness = 1.dp,   // Define the thickness of the line
                        color = Color.Gray // Set the color to grey
                    )

                    Text(
                        text = "Job Post id: ${jobUiState.jobPost_id}",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center job status
                    )


                    // If user has applied for this job then show the progress status
                    Text(
                        text = "Job Status: ${jobUiState.jobpost_status}",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center job status
                    )

                    Text(
                        text = "Work Type: " + jobUiState.jobpost_employment_type,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center work type
                    )

                    Text(
                        text = "Location: " + jobUiState.jobpost_company_address,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center location
                    )

                    Text(
                        text = "Salary: RM ${jobUiState.jobpost_salary_start} - RM ${jobUiState.jobpost_salary_end}",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center salary
                    )

                    Text(
                        text = "Posted Date: ${jobUiState.jobpost_start_date}",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center posted date
                    )

                    Text(
                        text = "Recruiter Id: ${jobUiState.recruiter_id}",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center posted date
                    )

                    Text(
                        text = "Jobpost status: " + jobUiState.jobpost_status,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center jobpost status
                    )

                    if (jobUiState.jobApplication_status != "Not Applied") {
                        Text(
                            text = "Application status: " + jobUiState.jobApplication_status,
                            fontSize = 14.sp,
                            color = if (jobUiState.jobApplication_status == "Approved") {
                                Color.Green
                            } else if (jobUiState.jobApplication_status == "Rejected") {
                                Color.Red
                            } else {
                                Color.Yellow
                            },
                            fontWeight = FontWeight.Bold,
                            lineHeight = 12.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally) // Center application status
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()  // Make the line span the full width
                            .padding(vertical = 12.dp),  // Optional padding
                        thickness = 1.dp,   // Define the thickness of the line
                        color = Color.Gray // Set the color to grey
                    )

                    Text(
                        text = "Descriptions: \n${jobUiState.jobpost_description}",
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center descriptions
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    if (jobUiState.jobpost_status != "Inactive") {
                        if (jobUiState.jobApplication_status == "Not Applied") {
                            Button(onClick = {
                                if (!resumeUiState.uploaded_resume_file_name.isNullOrEmpty()) {
                                    showApplyJobDialog = true
                                } else {
                                    showNeedResumeDialog = true
                                }
                            },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    disabledContentColor = Color.Gray,
                                    containerColor = colorResource(id = R.color.light_blue)
                                ),
                                shape = RoundedCornerShape(25),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally) // Center button
                                    .fillMaxWidth() // Adjust as necessary, or set a specific width/height
                                    .height(70.dp) // Set the height for the image if you want
                            ) {
                                Text(
                                    text = "APPLY NOW",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }

            }
            }








        }
        else{
            Text(text = "Job not found", color = colorResource(id = R.color.light_purple))

        }
    }


}


//
//@Preview(showBackground = true)
//@Composable
//private fun JobDetailScreenPreview() {
//    JobDetailScreen(jobseekerViewModel = JobseekerViewModel(), navController = rememberNavController())
//}