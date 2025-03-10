package com.example.jobseeker.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.ui.theme.JobseekerTheme
import com.example.jobseeker.ui.theme.purple2
import com.example.jobseeker.ui.theme.purple4

@Composable
fun StartAuthScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.background_purple))
            .padding(16.dp),
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
            text = "Welcome to JobHunt",
            fontSize = 36.sp,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            style = TextStyle(
                color = colorResource(id = R.color.light_purple),
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(AuthRouteScreen.JobseekerLogin.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = purple2),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(48.dp)
        ) {
            Text(text = "Login As Jobseeker", color = colorResource(id = R.color.light_purple))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(AuthRouteScreen.JobseekerSignUp.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = purple2),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(48.dp)
        ) {
            Text(text = "Sign Up as Jobseeker", color = colorResource(id = R.color.light_purple))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(AuthRouteScreen.RecruiterLogin.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = purple2),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(48.dp)
        ) {
            Text(text = "Login As Recruiter", color = colorResource(id = R.color.light_purple))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(AuthRouteScreen.RecruiterSignUp.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor = purple2),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(48.dp)
        ) {
            Text(text = "Sign Up As Recruiter", color = colorResource(id = R.color.light_purple))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartAuthScreenPreview(){

    JobseekerTheme {
        StartAuthScreen(navController = rememberNavController())
    }

}

