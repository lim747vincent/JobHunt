package com.example.jobseeker.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.RecruiterViewModel


@Composable
fun RecuiterProfileScreen(innerPadding: PaddingValues, rootNavController: NavController, recruiterViewModel: RecruiterViewModel, windowSize: WindowSize) {

    val fullName = recruiterViewModel.getRecruiterFullName
    val email = recruiterViewModel.getRecruiterEmail

    var showDialog by remember { mutableStateOf(false) }

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
        when(windowSize.height){
            WindowType.Medium->{
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Filled.Person
                        , contentDescription = "User Icon", tint =colorResource(id = R.color.light_purple) ,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 25.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Name: $fullName",
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.light_purple)
                        )

                        Text(
                            text = "Email: $email",
                            color = colorResource(id = R.color.light_purple) // Lighter color for email
                        )
                    }

                }
            } else ->{
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // Padding around the entire column
                horizontalAlignment = Alignment.CenterHorizontally, // Center everything horizontally
                verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between rows
            ) {
                // Icon at the top
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "User Icon",
                    tint = colorResource(id = R.color.light_purple),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp) // Spacing below the icon
                )

                // Full name in the middle
                Text(
                    text = "Name: $fullName",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.light_purple),
                    modifier = Modifier.padding(bottom = 8.dp) // Space between full name and email
                )

                // Email at the bottom
                Text(
                    text = "Email: $email",
                    color = colorResource(id = R.color.light_purple)
                )
            }
            }
        }



        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            rootNavController.navigate(Graph.MyCompanyGraph)
        }, modifier = Modifier.fillMaxWidth().padding(8.dp)


        ) {
            Text(text = "My Company")
        }



        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {

            showDialog = true

        }, modifier = Modifier.fillMaxWidth().padding(8.dp)

        ) {
            Text(text = "Logout")
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
                                showDialog = false
                            }
                        ) {
                            Text("No")
                        }

                        // Confirm Button
                        Button(
                            onClick = {
                                showDialog = false
                                recruiterViewModel.recruiterLoginEmail = ""
                                recruiterViewModel.recruiterLoginPassword = ""
                                rootNavController.navigate(Graph.AuthGraph) {
                                    popUpTo(Graph.MainScreenGraph) {
                                        inclusive = true
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