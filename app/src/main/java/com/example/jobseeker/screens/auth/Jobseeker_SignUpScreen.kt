package com.example.jobseeker.screens.auth

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.model.RecruiterUiState
import com.example.jobseeker.model.UserUiState
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.ui.theme.JobseekerTheme
import com.example.jobseeker.ui.theme.purple2
import com.example.jobseeker.ui.theme.purple4
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.google.firebase.firestore.auth.User

/**
 * Created 26-02-2024 at 05:08 pm
 */

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobseekerRegisterScreen(navController: NavController, jobseekerViewModel: JobseekerViewModel) {

    val context = LocalContext.current

    val UserUiState by jobseekerViewModel.uiState_Jobseeker.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {

                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(AuthRouteScreen.StartAuth.route) {
                            popUpTo(AuthRouteScreen.StartAuth.route) { inclusive = false }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(id = R.color.background_purple))
                .padding(16.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
//        Image(
//            painter = painterResource(id = R.drawable.logo),
//            contentDescription = null,
//            modifier = Modifier
//                .width(100.dp)
//                .height(100.dp)
//                .padding(bottom = 16.dp)
//        )
            Text(
                text = "Register As Jobseeker",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                style = TextStyle(
                    color = colorResource(id = R.color.light_purple),
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = UserUiState.jobseeker_full_name,
                onValueChange = { jobseekerViewModel.setJobseekerFullName(it) },
                label = { Text("Full Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.LightGray,
                    focusedLabelColor = Color.Blue,
                    unfocusedLabelColor = colorResource(id = R.color.light_purple),
                    focusedTextColor = colorResource(id = R.color.light_purple),
                    unfocusedTextColor = colorResource(id = R.color.light_purple)
                ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = UserUiState.jobseeker_email,
                onValueChange = { jobseekerViewModel.setJobseekerEmail(it) },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.LightGray,
                    focusedLabelColor = Color.Blue,
                    unfocusedLabelColor = colorResource(id = R.color.light_purple),
                    focusedTextColor = colorResource(id = R.color.light_purple),
                    unfocusedTextColor = colorResource(id = R.color.light_purple)
                ),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = UserUiState.jobseeker_password,
                onValueChange = { jobseekerViewModel.setJobseekerPassword(it) },
                label = { Text("Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.LightGray,
                    focusedLabelColor = Color.Blue,
                    unfocusedLabelColor = colorResource(id = R.color.light_purple),
                    focusedTextColor = colorResource(id = R.color.light_purple),
                    unfocusedTextColor = colorResource(id = R.color.light_purple)
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = jobseekerViewModel.repeatPassword,
                onValueChange = { jobseekerViewModel.repeatPassword = it },
                label = { Text("Confirm Password") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.White,
                    cursorColor = Color.LightGray,
                    focusedLabelColor = Color.Blue,
                    unfocusedLabelColor = colorResource(id = R.color.light_purple),
                    focusedTextColor = colorResource(id = R.color.light_purple),
                    unfocusedTextColor = colorResource(id = R.color.light_purple)
                ),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val jobseekerData = UserUiState(
                        jobseeker_full_name = UserUiState.jobseeker_full_name,
                        jobseeker_password = UserUiState.jobseeker_password,
                        jobseeker_email = UserUiState.jobseeker_email
                    )

                    if(UserUiState.jobseeker_full_name.isBlank() || UserUiState.jobseeker_email.isBlank() || UserUiState.jobseeker_password.isBlank()){
                        Toast.makeText(context, "Please fill up all the fields.", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if(!UserUiState.jobseeker_email.matches(jobseekerViewModel.emailPattern)){
                        Toast.makeText(context, "Please ensure that email is in correct format(e.g. abc@gmail.com)", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    jobseekerViewModel.isEmailRegistered(UserUiState.jobseeker_email) { isRegistered ->
                        if (isRegistered) {
                            Toast.makeText(context, "Email already registered.", Toast.LENGTH_SHORT).show()
                        } else {
                            if (jobseekerViewModel.isMatched()) {
                                val profile = UserUiState(
                                    jobseeker_email = UserUiState.jobseeker_email,
                                    jobseeker_password = UserUiState.jobseeker_password,
                                    jobseeker_full_name = UserUiState.jobseeker_full_name
                                )

                                jobseekerViewModel.saveData(jobseekerData = jobseekerData, context)
                                jobseekerViewModel.getJobseeker(UserUiState.jobseeker_email)
                                jobseekerViewModel.setProfile(profile)
                                navController.navigate(Graph.MainScreenGraph) {
                                    popUpTo(AuthRouteScreen.StartAuth.route) { inclusive = true }
                                }
                            } else {
                                Toast.makeText(
                                    context,
                                    "Please ensure that password is matched!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = purple2),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(48.dp),
            ) {
                Text(text = "Register", fontSize = 12.sp, color = colorResource(id = R.color.light_purple))
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {




                    navController.navigate(AuthRouteScreen.JobseekerLogin.route){
                        navController.navigateUp()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(text = "Already have an jobseeker account? Login", color = colorResource(id = R.color.light_purple))
            }
        }


    }


}


//@Preview(showBackground = true)
//@Composable
//fun JobseekerRegisterScreenPreview(){
//
//    JobseekerTheme {
//        JobseekerRegisterScreen(navController = rememberNavController())
//    }
//
//}

