package com.example.jobseeker.model

data class ApplicantFilteringUiState(
    val selectedApplicantStatus: String = "Select Applicant Status",
    val jobPostingId: String = "",
    val jobPostingDate: String = ""
)
