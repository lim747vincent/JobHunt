package com.example.jobseeker.model

import androidx.annotation.DrawableRes

data class JobSeekerApplicationUiState(
    val id: String = "",
    val title: String = "",
    val company: String = "",
    val location: String = "",
    val type: String = "",
    val selected: Boolean = false,
    @DrawableRes val imageResourceId: Int = 0
)