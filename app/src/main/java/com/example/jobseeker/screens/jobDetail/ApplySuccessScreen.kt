package com.example.jobseeker.screens.jobDetail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.viewModel.JobseekerViewModel


@Composable
fun ApplySuccessScreen(
    navController: NavController
) {
    // Disable back button
    BackHandler {
        // Do nothing to disable the back button
    }

    // LaunchedEffect to perform action after a delay
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000L) // 3 seconds delay

        navController.navigate(Graph.MainScreenGraph){
            popUpTo(Graph.JobDetailGraph) {
                inclusive = true // Removes all destinations in the MainNavGraph, including the start destination
            }

            launchSingleTop = true
        } // Navigate to another screen
    }

    // The UI of the Thank You Screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.background_purple)), // Set background color
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Thank You Icon
            Icon(
                imageVector = Icons.Default.ThumbUp, // You can use any other appropriate icon
                contentDescription = "Thank You",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp),
                tint = colorResource(id = R.color.light_purple) // Color of the icon
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Thank You Message
            Text(
                text = "Thank You for Your Submission!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.light_purple)
            )
            Text(
                text = "Redirect in 3 sec...",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = colorResource(id = R.color.light_purple)
            )
        }
    }


}




@Preview(showBackground = true)
@Composable
private fun ApplySuccessScreenPreview() {
    ApplySuccessScreen(navController = rememberNavController())
}