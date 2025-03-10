package com.example.jobseeker

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.jobseeker.navigations.graphs.RootNavGraph
import com.example.jobseeker.ui.theme.JobseekerTheme
import com.example.jobseeker.utils.rememberWindowSize
import com.example.jobseeker.viewModel.RecruiterViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            JobseekerTheme {
                val window = rememberWindowSize()
                RootNavGraph(false, windowSize = window)
            }
        }
    }
}
