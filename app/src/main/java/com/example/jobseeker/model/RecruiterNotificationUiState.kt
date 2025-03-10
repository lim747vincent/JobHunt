package com.example.jobseeker.model

data class RecruiterNotificationUiState(
    val recruiterNotification_id: String = "",
    val recruiter_notification_title: String = "",
    val recruiter_notification_date: String ="asd",
    val recruiter_notification_brief_text: String="",
    val recruiter_notification_detail_text: String="",
    val recruiter_notification_sender: String="",
    val recruiter_notification_isRead: Boolean = false,
    val recruiter_id: String="",
    val jobpost_id: String="",
)
