package com.example.jobseeker.model

data class CreditScoreTransactionUiState(
    val credit_score_transaction_title : String = "",
    val credit_score_topup_amount : String = "",
    val credit_score_payment_method : String = "",
    val credit_score_transaction_date : String = "",
    val recruiter_id : String = ""
)