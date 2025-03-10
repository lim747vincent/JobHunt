package com.example.jobseeker.model

data class JobFilteringFormUiState(
    val jobTitle: String = "",
    val selectedEmploymentType: String = "Select Option",
    val selectedLocation: String = "Select Option",
    val salaryRange: ClosedFloatingPointRange<Float> = 0f..10000f
)