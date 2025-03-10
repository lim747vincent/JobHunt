package com.example.jobseeker.screens.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jobseeker.R
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterNotificationDetailScreen(navController: NavController, recruiterViewModel: RecruiterViewModel, windowSize: WindowSize) {
    val notificationUiState by recruiterViewModel.uiState_SelectedNotification.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.top_bar_color)),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(20.dp), // Fill the width of the TopAppBar
//                        contentAlignment = Alignment.Center // Center the content
                    ) {
                        Text(
//                            text = "Notification Details Screen",
                            text = notificationUiState.recruiter_notification_title,
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
            modifier = Modifier .background(colorResource(id = R.color.background_purple))
                .padding(innerPadding).padding(horizontal = 16.dp, vertical = 30.dp)
                .fillMaxSize()
            ,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if(notificationUiState != null){

                when(windowSize.height){
                    WindowType.Medium->{
                        Text(
                            text = "Title: " + notificationUiState.recruiter_notification_title,
                            fontSize = 32.sp,
                            lineHeight = 30.sp,
                            color = colorResource(id = R.color.light_purple)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Detail text: " + notificationUiState.recruiter_notification_detail_text,
                            fontSize = 20.sp,
                            color = colorResource(id = R.color.light_purple)
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = "Date: " + notificationUiState.recruiter_notification_date,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )
                        Text(
                            text = "Sender: " + notificationUiState.recruiter_notification_sender,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )
                        Text(
                            text = "Jobpost id: " + notificationUiState.jobpost_id,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.light_purple)
                        )
                    }else -> {
                    Text(
                        text = "Title: " + notificationUiState.recruiter_notification_title,
                        fontSize = 32.sp,
                        color = colorResource(id = R.color.light_purple),
                        lineHeight = 30.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center title
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Detail text: " +notificationUiState.recruiter_notification_detail_text,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center detail text
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Date: " +notificationUiState.recruiter_notification_date,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center notification date
                    )
                    Text(
                        text = "Sender: " +notificationUiState.recruiter_notification_sender,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple),
                        modifier = Modifier.align(Alignment.CenterHorizontally) // Center sender text
                    )
                    Text(
                        text = "Jobpost id: " + notificationUiState.jobpost_id,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.light_purple)
                    )
                }
                }


            } else{
                Text(text = "Notification not found", color = colorResource(id = R.color.light_purple))

            }



        }
    }
}

//
//@Preview(showBackground = true)
//@Composable
//fun NotificationDetailScreenPreview(){
//
//    JobseekerTheme {
//        NotificationDetailScreen(rememberNavController(), JobseekerViewModel())
//    }
//
//}