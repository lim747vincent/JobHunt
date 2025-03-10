package com.example.jobseeker.component

import android.content.Context
import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobseeker.R
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.utils.WindowType
import com.example.jobseeker.viewModel.JobseekerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.DecimalFormat

@Composable
fun JobListCard(jobListingUiState: JobListingUiState, modifier: Modifier = Modifier, jobseekerViewModel: JobseekerViewModel, context: Context, windowSize: WindowSize, onClick: ()->Unit) {
    var selected = remember { mutableStateOf(jobListingUiState.isFavor) }

    val profileUiState by jobseekerViewModel.uiState_Profile.collectAsState()

    val decimalFormat = DecimalFormat("#.##")

    var showAlert by remember { mutableStateOf(false) }

    val imageUrl = jobListingUiState.jobpost_company_logo_image_filepath

    // Alert dialog
    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text(text = "Alert!") },
            text = { Text(text = "You cannot save inactive job post.") },
            confirmButton = {
                Button(onClick = { showAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    when (windowSize.height){
        WindowType.Medium ->{
            ElevatedCard(colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.hard_purple)),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Surface(color = Color.Transparent,
                    modifier = Modifier.padding(20.dp)){
                    Column (modifier= Modifier
                        .fillMaxWidth()) {
                        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
//                    Image(painter =  painterResource(id = jobListingUiState.imageResourceId), contentDescription = "logo",contentScale = ContentScale.Crop,
//                        modifier = Modifier.clip(RoundedCornerShape(5.dp)))
                            Column(modifier = Modifier.weight(1f).fillMaxWidth()){
                                CustomImageView(imageUrl = imageUrl)

                            }

                            IconButton(
                                modifier = Modifier.wrapContentWidth(),
                                onClick = {

                                    if(jobListingUiState.jobpost_status == "Inactive"){
                                        showAlert = true
                                    }else{
                                        showAlert = false

                                        selected.value = !selected.value

                                        var notice = "";
                                        if(selected.value == true){

                                            CoroutineScope(Dispatchers.Main).launch {
                                                jobseekerViewModel.addSavedJob(jobseekerId = profileUiState.jobseeker_email, jobpostId = jobListingUiState.jobPost_id)
                                            }


                                            notice = "It is favored!";
                                        } else{

                                            CoroutineScope(Dispatchers.Main).launch {
                                                jobseekerViewModel.removeSavedJob(jobseekerId = profileUiState.jobseeker_email, jobpostId = jobListingUiState.jobPost_id)
                                            }


                                            notice = "It is unfavored!";
                                        }

                                        Toast.makeText(context, notice , Toast.LENGTH_SHORT).show()
                                    }

                                }
                            ) {
                                if(selected.value == false ){
                                    Image(painter =  painterResource(id = R.drawable.heart), contentDescription = "heart",contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(30.dp))
                                } else{
                                    Image(painter =  painterResource(id = R.drawable.heart_selected), contentDescription = "heart",contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(30.dp))
                                }
                            }




                        }

                        Spacer(modifier= Modifier.height(15.dp))
                        Text(
                            text = jobListingUiState.jobpost_title,
                            color = colorResource(id = R.color.light_purple),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp

                        )
                        Text(
                            text = jobListingUiState.jobpost_company_name,
                            color = colorResource(id = R.color.light_purple)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(
                            text = jobListingUiState.jobpost_employment_type,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_purple),
                            lineHeight = 12.sp
                        )
                        Text(
                            text = jobListingUiState.jobpost_company_state,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_purple),
                            lineHeight = 12.sp
                        )
                        Text(
                            text = "RM ${decimalFormat.format(jobListingUiState.jobpost_salary_start)} - RM ${decimalFormat.format(jobListingUiState.jobpost_salary_end)} ",
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_purple),
                            lineHeight = 12.sp
                        )
                        Text(
                            text = "Jobpost status: " +jobListingUiState.jobpost_status,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.light_purple),
                            lineHeight = 12.sp
                        )
                        if(jobListingUiState.jobApplication_status != "Not Applied"){
                            Text(
                                text = "Application status: " +jobListingUiState.jobApplication_status,
                                fontSize = 14.sp,
                                color = if(jobListingUiState.jobApplication_status == "Approved"){
                                    Color.Green
                                }else if(jobListingUiState.jobApplication_status == "Rejected"){
                                    Color.Red
                                }else{
                                    Color.Yellow
                                }
                                ,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = jobListingUiState.jobpost_start_date,
                                color = colorResource(id = R.color.grey)
                            )
                            Button(onClick = {         onClick() },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.White,
                                    disabledContentColor = Color.Gray,
                                    containerColor = colorResource(id = R.color.light_blue)
                                )
                            ) {
                                Text(
                                    text="View",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.width(70.dp)
                                )
                            }
                        }


                    }
                }
            }
        } else ->{
        ElevatedCard(
            colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.hard_purple)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Surface(
                color = Color.Transparent,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Image section
                    CustomImageView(imageUrl = imageUrl) // Assuming CustomImageView handles image loading

                    Spacer(modifier = Modifier.height(8.dp))

                    // Title section
                    Text(
                        text = jobListingUiState.jobpost_title,
                        color = colorResource(id = R.color.light_purple),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // View button section
                    Button(
                        onClick = { onClick() },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = colorResource(id = R.color.light_blue)
                        ),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text(
                            text = "View",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
    }





}



//@Preview
//@Composable
//private fun JobListCardPreview() {
//    JobListCard(JobListingUiState(title = "title1", company = "company 1", location = "location 1", type = "type 1", imageResourceId = R.drawable.intellogo, selected = false), context= LocalContext.current)
//}