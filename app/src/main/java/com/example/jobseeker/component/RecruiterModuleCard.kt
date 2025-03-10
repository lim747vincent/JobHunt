package com.example.jobseeker.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType

@Composable
fun RecuiterModuleCard(title: String, iconId: Int, onClickView: () -> Unit, windowSize:WindowSize){
    //var selected = remember { mutableStateOf(recuiterModule.selected) }

    when(windowSize.height){
        WindowType.Medium->{
            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3C3C70)
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                        colorFilter = ColorFilter.tint(color = colorResource(id = R.color.light_purple))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = { onClickView() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF))
                    ) {
                        Text(text = "View", color = colorResource(id = R.color.light_purple))
                    }
                }
            }
        } else ->{
        Card(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3C3C70)
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally, // Center the content horizontally
                verticalArrangement = Arrangement.spacedBy(16.dp) // Spacing between rows
            ) {
                // Image Row
                Image(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp), // Adjusted size
                    colorFilter = ColorFilter.tint(color = colorResource(id = R.color.light_purple))
                )

                // Text Row
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center // Center text horizontally
                )

                // Button Row
                Button(
                    onClick = { onClickView() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF)),
                    modifier = Modifier.fillMaxWidth() // Button fills the width of the row
                ) {
                    Text(text = "View", color = colorResource(id = R.color.light_purple))
                }
            }
        }
        }
    }








}

@Composable
fun RecuiterModuleCardList(rootNavController: NavController, windowSize:WindowSize){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    RecuiterModuleCard(
                        title = "Credit Score Module",
                        iconId = R.drawable.baseline_money,
                        onClickView = { rootNavController.navigate(Graph.CreditScoreModule) },
                        windowSize = windowSize
                    )
                    RecuiterModuleCard(
                        title = "Job Posting Module",
                        iconId = R.drawable.baseline_draw,
                        onClickView = { rootNavController.navigate(Graph.JobPostingModule) },
                        windowSize = windowSize
                    )
                    RecuiterModuleCard(
                        title = "Application Module",
                        iconId = R.drawable.baseline_account_circle,
                        onClickView = { rootNavController.navigate(Graph.ApplicationModule) },
                        windowSize = windowSize
                    )
                }







    }

}


//@Preview
//@Composable
//private fun RecuiterModuleCardPreview(){
//    RecuiterModuleCardList(rootNavController = rememberNavController())
//}