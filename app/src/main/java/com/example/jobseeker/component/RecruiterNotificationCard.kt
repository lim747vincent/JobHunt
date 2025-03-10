package com.example.jobseeker.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobseeker.R
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.model.RecruiterNotificationUiState
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel

@Composable
fun RecruiterNotificationCard(notification: RecruiterNotificationUiState, modifier: Modifier = Modifier, recruiterViewModel: RecruiterViewModel,
                              onClick:()->Unit) {
    var isRead = remember { mutableStateOf(notification.recruiter_notification_isRead) }

    val onClickNotification = {
        if(isRead.value == false){
            isRead.value = true
            recruiterViewModel.updateRecruiterNotificationStatus(notification)
        }
    }


    Card(colors = CardDefaults.cardColors(
        containerColor = colorResource(id = R.color.hard_purple),
    ), shape= RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth().padding(bottom = 20.dp).padding(horizontal = 20.dp).clickable {
                onClickNotification()
                onClick()

            }
    ) {
        Column(modifier= Modifier
            .fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                Text(text = notification.recruiter_notification_title,fontSize=25.sp, color= if(isRead.value!=true){
                    colorResource(id = R.color.light_purple)
                }else{
                    colorResource(id = R.color.grey)
                },  fontWeight = if (isRead.value != true){
                    FontWeight(800)
                }else{
                    FontWeight(400)
                })
            }
            Spacer(modifier= Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Start){
                Text(text = notification.recruiter_notification_brief_text,color = if(isRead.value!=true){
                    colorResource(id = R.color.light_purple)
                }else{
                    colorResource(id = R.color.grey)
                }, fontWeight = if (isRead.value != true){
                    FontWeight(800)
                }else{
                    FontWeight(400)
                })
            }
            Spacer(modifier= Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){
                Text(text = notification.recruiter_notification_sender,color = if(isRead.value!=true){
                    colorResource(id = R.color.light_purple)
                }else{
                    colorResource(id = R.color.grey)
                }, fontWeight = if (isRead.value != true){
                    FontWeight(800)
                }else{
                    FontWeight(400)
                })
                Text(text = notification.recruiter_notification_date,color = if(isRead.value!=true){
                    colorResource(id = R.color.light_purple)
                }else{
                    colorResource(id = R.color.grey)
                }, fontWeight = if (isRead.value != true){
                    FontWeight(800)
                }else{
                    FontWeight(400)
                })
            }
        }


    }


}



//@Preview
//@Composable
//private fun NotificationCardPreview() {
//    NotificationCard(JobseekerNotification(title="title 1", date = "1-1-2022", briefText = "brief text", detailText="detailText",
//    sender = "sender",
//    isRead=true
//    ))
//}