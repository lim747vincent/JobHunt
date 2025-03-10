package com.example.jobseeker.model

data class JobPostingUiState(
    val jobPost_id : String = "",
    val jobpost_company_address : String = "",
    val jobpost_company_logo_image_filepath : String = "",
    val jobpost_company_name : String = "",
    val jobpost_company_state : String = "",
    val jobpost_company_size : String = "",
    val jobpost_company_industry : String = "",
    val jobpost_description : String = "",
    val jobpost_employment_type : String = "",
    val jobpost_end_date : String = "",
    val jobpost_salary_end : Double = 0.0,
    val jobpost_salary_start : Double = 0.0,
    val jobpost_start_date : String = "",
    val jobpost_status : String = "",
    val jobpost_title : String = "",
    val recruiter_id : String = "",
)
