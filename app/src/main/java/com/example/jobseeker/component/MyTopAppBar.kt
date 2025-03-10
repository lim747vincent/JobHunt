package com.example.jobseeker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.JobDetailRouteScreen

@Composable
fun MyTopAppBar(pageTitle:String, rootNavController: NavController, recruiterEmail: String?
) {
    if(pageTitle != "Home"){
        Row(
            Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.top_bar_color))
                .statusBarsPadding()
                .padding(start = 33.dp, end = 33.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(80.dp),
                painter = painterResource(id = R.drawable.jobhuntlogo),
                contentDescription = null,
            )

        }
    } else{
        if(recruiterEmail.isNullOrEmpty()){
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.top_bar_color))
                    .statusBarsPadding()
                    .padding(start = 33.dp, end = 33.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = CenterVertically
            ) {
                Spacer(modifier = Modifier.width(40.dp))
                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = painterResource(id = R.drawable.jobhuntlogo),
                    contentDescription = null,
                )
                Icon(
                    modifier = Modifier
                        .size(30.dp).clickable(onClick = {
                            rootNavController.navigate(JobDetailRouteScreen.JobFiltering.route)
                        }),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = colorResource(id = R.color.search_icon_color)
                )
            }
        }else{
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.top_bar_color))
                    .statusBarsPadding()
                    .padding(start = 33.dp, end = 33.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = painterResource(id = R.drawable.jobhuntlogo),
                    contentDescription = null,
                )

            }
        }

    }

}