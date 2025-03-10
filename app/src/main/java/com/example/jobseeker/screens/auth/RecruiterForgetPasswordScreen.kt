package com.example.jobseeker.screens.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.ui.theme.purple2
import com.example.jobseeker.ui.theme.purple4
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterForgotPasswordScreen(navController: NavController) {
    var recruiter_email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                .padding(16.dp)
                .background(purple4)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Forgot Password ?",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = recruiter_email,
                onValueChange = { recruiter_email = it },
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Blue, // Color when the field is focused
                    unfocusedBorderColor = Color.White, // Color when the field is not focused
                    cursorColor = Color.LightGray, // Cursor color
                    focusedLabelColor = Color.Blue, // Label color when focused
                    unfocusedLabelColor = Color.White, // Label color when not focused
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    when {
                        // Check if email is empty
                        recruiter_email.isEmpty() -> {
                            Toast.makeText(context, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                        // Validate email format
                        !isValidEmail(recruiter_email) -> {
                            Toast.makeText(context, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                        else -> {
                            resetRecruiterPassword(auth, recruiter_email, context) { success, errorMessage ->
                                isLoading = false
                                if (success) {
                                    Toast.makeText(context, "Please check your email!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, errorMessage ?: "An error occurred", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = purple2),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .height(48.dp)
            ) {
                Text(text = if (isLoading) "Sending..." else "Reset Password", color = Color.White)
            }

        }
    }
}

fun resetRecruiterPassword(auth: FirebaseAuth, email: String, context: Context, callback: (Boolean, String?) -> Unit) {
    auth.sendPasswordResetEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callback(true, null)  // Success
            } else {
                val exceptionMessage = task.exception?.message
                // Log the error message for debugging purposes
                Toast.makeText(context, "Failed: $exceptionMessage", Toast.LENGTH_LONG).show()
                callback(false, exceptionMessage)  // Error message
            }
        }
}