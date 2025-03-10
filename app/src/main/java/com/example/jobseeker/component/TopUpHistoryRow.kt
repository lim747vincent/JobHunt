package com.example.jobseeker.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobseeker.R
import com.example.jobseeker.model.CreditScoreTransactionUiState

@Composable
fun TopUpHistoryRow(transaction: CreditScoreTransactionUiState){
    Row(
        modifier = Modifier
            .padding(
                start = dimensionResource(R.dimen.padding_30dp),
                top = dimensionResource(R.dimen.padding_5dp)
            )
    ){
        Column(){
            Text(
                text = "${transaction.credit_score_transaction_title} via ${transaction.credit_score_payment_method}",
                color = colorResource(R.color.light_purple),
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    end = dimensionResource(R.dimen.padding_30dp)
                ),
            horizontalAlignment = Alignment.End
        ){
            Text(
                text = transaction.credit_score_topup_amount,
                color = colorResource(R.color.light_purple)
            )
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                end = dimensionResource(R.dimen.padding_30dp),
                bottom = dimensionResource(R.dimen.padding_5dp)
            ),
        horizontalArrangement = Arrangement.End
    ){
        Text(
            text = transaction.credit_score_transaction_date,
            color = colorResource(R.color.light_purple),
            fontSize = 14.sp
        )
    }
    HorizontalDivider(
        modifier = Modifier
            .padding(
                start = dimensionResource(R.dimen.padding_30dp),
                end = dimensionResource(R.dimen.padding_30dp)
            ),
        thickness = 1.dp
    )
}