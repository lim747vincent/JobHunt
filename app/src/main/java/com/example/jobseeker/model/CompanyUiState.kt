package com.example.jobseeker.model

data class CompanyUiState(
    val company_name : String = "",
    val company_description : String = "",
    val company_state : String = "Select Location",
    val company_address : String = "",
    val company_size : String = "",
    val company_industry : String = "",
    val company_logo_image : String = "",
    val recruiter_id : String = ""
)
