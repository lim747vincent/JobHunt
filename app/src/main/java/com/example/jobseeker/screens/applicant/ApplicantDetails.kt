package com.example.jobseeker.screens.applicant

import android.app.DownloadManager
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import coil.compose.rememberImagePainter
import com.example.jobseeker.R
import com.example.jobseeker.viewModel.RecruiterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.applyConnectionSpec
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicantDetails(
    rootNavController: NavController,
    recruiterViewModel: RecruiterViewModel,
    jobApplicationId : String) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        recruiterViewModel.fetchJobApplicationById(jobApplicationId)
    }

    val jobApplication = recruiterViewModel.jobApplication.collectAsState()
    val jobPost = recruiterViewModel.fetchedJobPost.collectAsState()
    val jobSeeker = recruiterViewModel.jobseeker.collectAsState()

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
                            text = "Applicant Details",
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
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top, // Align to the top
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Profile Section
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = CircleShape)
                    .border(2.dp, color = Color.Gray, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "User Icon",
                    modifier = Modifier.size(50.dp),
                    tint = colorResource(id = R.color.background_purple)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            jobApplication.value?.let{ application ->
                val jobSeekerId = application.jobseeker_id
                val applicationStatus = application.jobApplication_status
                val jobPostId = application.jobpost_id
                recruiterViewModel.fetchUploadedResumeUrl(jobSeekerId)
                recruiterViewModel.fetchJobPostById(jobPostId)
                recruiterViewModel.fetchJobSeekerById(jobSeekerId)

                jobPost.value?.let { jobpost ->
            jobSeeker.value.let { jobseeker ->

                var applicationStatusOnScreen by remember {mutableStateOf(application.jobApplication_status) }
                // Applicant Info
                Text(
                    text = jobseeker.jobseeker_full_name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
                Text(text = jobSeekerId, fontSize = 16.sp, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            // Download Resume Button
            Button(
                onClick = { downloadFile(recruiterViewModel.uploadedResumeUrl.value, context) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Text(text = "Download Resume", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Section
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(text = "Progress", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))

                // Applied Section
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(R.drawable.approved),
                            contentDescription = applicationStatus,
                            tint = Color.Green
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = applicationStatusOnScreen, color = Color.Black)
                        Spacer(modifier = Modifier.width(45.dp))
                    }
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Job Applied Section

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(text = "Job Applied", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = rememberImagePainter(jobpost.jobpost_company_logo_image_filepath),
                        contentDescription = "Company Logo"
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = jobpost.jobpost_title, fontWeight = FontWeight.Bold, color = Color.Black)
                Text(text = jobpost.jobpost_company_name, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = jobpost.jobpost_company_state, color = Color.Gray)
                Text(text = "${jobpost.jobpost_employment_type} | RM ${jobpost.jobpost_salary_start} - RM ${jobpost.jobpost_salary_end} per month",
                    color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))



            // Reject and Approve Buttons
                if(applicationStatusOnScreen == "Processing"){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                recruiterViewModel.updateJobApplicationStatus(jobApplicationId, "Rejected", jobpost.jobpost_title, jobpost.recruiter_id, application.jobseeker_id, jobpost.jobPost_id,context)
                                applicationStatusOnScreen = "Rejected"
                                      },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            modifier = Modifier.fillMaxWidth(0.3f)
                        ) {
                            Text(text = "Reject", color = Color.White)
                        }

                        Button(
                            onClick = {
                                recruiterViewModel.updateJobApplicationStatus(jobApplicationId, "Approved", jobpost.jobpost_title, jobpost.recruiter_id, application.jobseeker_id, jobpost.jobPost_id, context)
                                applicationStatusOnScreen = "Approved"
                                      },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                            modifier = Modifier.fillMaxWidth(0.4f)
                        ) {
                            Text(text = "Approve", color = Color.White)
                        }
                    }
                }

            }
            }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun downloadFile(url: String, context: android.content.Context) {
    if (url.isNotEmpty()) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder().url(url).build()
                val response = OkHttpClient().newCall(request).execute()

                if (response.isSuccessful) {
                    val inputStream = response.body?.byteStream()

                    // Use MediaStore to save the file
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, "resume.pdf")
                        put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                        put(MediaStore.Downloads.RELATIVE_PATH, "Download")
                    }
                    val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

                    uri?.let { uri ->
                        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                            inputStream?.copyTo(outputStream)
                        }
                    }

                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Download complete", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Download failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.d("DownloadError", "Download error: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    } else {
        Toast.makeText(context, "No URL available", Toast.LENGTH_SHORT).show()
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
@Preview
@Composable
private fun ApplicantDetailsPreview() {
    ApplicantDetails(rootNavController = rememberNavController(), recruiterViewModel = viewModel(), jobApplicationId = "")
}
