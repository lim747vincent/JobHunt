package com.example.jobseeker.screens.main

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.screens.auth.JobseekerLoginScreen
import com.example.jobseeker.ui.theme.JobseekerTheme
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


@Composable
fun JobseekerProfileScreen(innerPadding: PaddingValues, rootNavController: NavController, jobseekerViewModel: JobseekerViewModel, windowSize: WindowSize) {

    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    val resumeUiState by jobseekerViewModel.uiState_Resume.collectAsState()

    val isLoading by jobseekerViewModel.isLoading.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    var showAddJobDialog by remember { mutableStateOf(false) }

    var showDeleteJobDialog by remember { mutableStateOf(false) }

    var showNeedResumeDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val pickPdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                // Proceed to upload the PDF to Firebase Storage

                jobseekerViewModel.viewModelScope.launch {
                    jobseekerViewModel.uploadPdfToFirebase(uri = it, jobseekerId = profileUiState.jobseeker_email, context=  context)
                }
            }
        }
    )


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
                            put(MediaStore.Downloads.DISPLAY_NAME, "Your_resume.pdf")
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
        if (isLoading && profileUiState.jobseeker_email.isNullOrEmpty()) {
            // Centered CircularProgressIndicator
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp), // Change the size
                    color = colorResource(id = R.color.light_purple), // Set custom color
                    strokeWidth = 8.dp // Set custom stroke width
                )
            }
        }else{
            Row(
                modifier = Modifier.fillMaxWidth(), // Makes the Row fill the width of the parent
                horizontalArrangement = Arrangement.Center, // Centers horizontally
                verticalAlignment = Alignment.CenterVertically // Centers vertically
            ){
                Icon(
                    imageVector = Icons.Filled.Person
                    , contentDescription = "User Icon", tint =colorResource(id = R.color.light_purple) ,
                    modifier = Modifier
                        .size(100.dp) // Set the size of the icon
                        .padding(end = 25.dp) // Space between icon and text
                )

                Column(
                    verticalArrangement = Arrangement.Center
                ) {

                    // User Name
                    Text(
                        text = "Name: ${profileUiState.jobseeker_full_name}",
                        color = colorResource(id = R.color.light_purple)
                    )

                    // User Email
                    Text(
                        text = "Email: ${profileUiState.jobseeker_email}",
                        color = colorResource(id = R.color.light_purple) // Lighter color for email
                    )

                    Text(
                        text = if (resumeUiState.uploaded_resume_file_name.isNullOrEmpty()){
                            "Resume: N/A"
                        } else{
                            "Resume: ${resumeUiState.uploaded_resume_file_name}"
                        }
                        ,
                        color = colorResource(id = R.color.light_purple) // Lighter color for email
                        ,fontSize = 15.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(20.dp))


            Button(onClick = {

                if(!resumeUiState.uploaded_resume_file_name.isNullOrEmpty()){

                    downloadFile(resumeUiState.uploaded_resume_file_url, context)


//                    val intent = Intent(Intent.ACTION_VIEW).apply {
//                        data = Uri.parse(resumeUiState.uploaded_resume_file_url)
//                        setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//                    }
//                    context.startActivity(intent)

                }else{

                    showNeedResumeDialog = true

                }



            }, modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    containerColor = colorResource(id = R.color.light_blue)
                )


            ) {
                Text(text = "Download my resume")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                if(resumeUiState.uploaded_resume_file_name.isNullOrEmpty()){
                    pickPdfLauncher.launch("application/pdf")

                }else{

                    showAddJobDialog = true

                }
            }, modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    containerColor = colorResource(id = R.color.light_blue)
                )


            ) {
                Text(text = "Upload resume")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                if(!resumeUiState.uploaded_resume_file_name.isNullOrEmpty()){

                    showDeleteJobDialog = true

                }else{

                    showNeedResumeDialog = true

                }



            }, modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    containerColor = colorResource(id = R.color.light_blue)
                )


            ) {
                Text(text = "Delete resume")
            }



            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                showDialog = true

            }, modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    disabledContentColor = Color.Gray,
                    containerColor = colorResource(id = R.color.light_blue)
                )

            ) {
                Text(text = "Logout")
            }

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


            if (showAddJobDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showAddJobDialog = false // Dismiss dialog when clicked outside
                    },
                    title = {
                        Text(text = "Alert")
                    },
                    text = {
                        Text("Each jobseeker can upload 1 resume. Please remove your existing uploaded resume to upload new file.")
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
                                    showAddJobDialog = false // Just close the dialog if "No" is clicked
                                }
                            ) {
                                Text("Ok")
                            }
                        }
                    }

                )
            }

            if (showDeleteJobDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteJobDialog = false // Dismiss dialog when clicked outside
                    },
                    title = {
                        Text(text = "Confirm Delete Resume")
                    },
                    text = {
                        Text("Are you sure you want to delete uploaded resume?")
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
                                    showDeleteJobDialog = false // Just close the dialog if "No" is clicked
                                }
                            ) {
                                Text("No")
                            }

                            // Confirm Button
                            Button(
                                onClick = {
                                    showDeleteJobDialog = false // Dismiss dialog
                                    jobseekerViewModel.deleteResumeFile(fileUrl = resumeUiState.uploaded_resume_file_url, jobseekerId = profileUiState.jobseeker_email, context= context)


                                }
                            ) {
                                Text("Yes")
                            }
                        }
                    }

                )
            }


            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false // Dismiss dialog when clicked outside
                    },
                    title = {
                        Text(text = "Confirm Logout")
                    },
                    text = {
                        Text("Are you sure you want to logout?")
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
                                    showDialog = false // Just close the dialog if "No" is clicked
                                }
                            ) {
                                Text("No")
                            }

                            // Confirm Button
                            Button(
                                onClick = {
                                    showDialog = false // Dismiss dialog

                                    jobseekerViewModel.resetViewModel()

                                    rootNavController.navigate(Graph.AuthGraph) {
                                        popUpTo(Graph.MainScreenGraph) {
                                            inclusive = true // Removes all destinations in MainNavGraph, including the start destination
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            ) {
                                Text("Yes")
                            }
                        }
                    }

                )
            }








        }

    }
}


//@Preview(showBackground = true)
//@Composable
//fun JobseekerProfileScreenPreview(){
//
//    JobseekerTheme {
//        JobseekerProfileScreen(innerPadding = 10.dp, rootNavController = rememberNavController())
//    }

