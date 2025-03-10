package com.example.jobseeker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.jobseeker.R

@Composable
fun RecuiterTopBar(pageTitle:String){
    Row(
        Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.light_purple))
            .statusBarsPadding()
            .padding(start = 33.dp, end = 33.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(80.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            )

        }
}