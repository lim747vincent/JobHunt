package com.example.jobseeker.model

data class JobseekerNotificationUiState(
    val jobseekerNotification_id: String = "",
    val jobseeker_notification_title: String = "",
    val jobseeker_notification_date: String ="asd",
    val jobseeker_notification_brief_text: String="",
    val jobseeker_notification_detail_text: String="",
    val jobseeker_notification_sender: String="",
    val jobseeker_notification_isRead: Boolean = false,
    val jobseeker_id: String="",
    val jobpost_id: String =""
)