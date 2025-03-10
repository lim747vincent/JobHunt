package com.example.jobseeker.screens.creditscore

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.viewModel.RecruiterViewModel
import kotlinx.coroutines.delay

@Composable
fun CreditTopUpSuccessfully(rootNavController: NavController, recruiterViewModel: RecruiterViewModel){

    var displayText by remember {
        mutableStateOf("Processing...")
    }

    LaunchedEffect(Unit){
        delay(2500)
        displayText = "Top up Successfully!"
        delay(2500)
        rootNavController.navigate(Graph.CreditScoreModule){
            popUpTo(Graph.CreditScoreModule){ inclusive = false }

            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                modifier = Modifier
                    .size(150.dp),
                painter = painterResource(R.drawable.intro),
                contentDescription = null
            )
            Spacer(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_15dp))
            )
            Text(
                text = displayText,
                color = colorResource(R.color.font_color),
                fontSize = 36.sp
            )
        }
    }
}
