package com.example.jobseeker.screens.creditscore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobseeker.R
import com.example.jobseeker.component.TopUpHistoryRow
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScoreHistory(
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    recruiterViewModel: RecruiterViewModel
){
    var isAscending by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        recruiterViewModel.fetchTransactions(recruiterViewModel.recruiterLoginEmail)
    }

    val transactions = recruiterViewModel.transactions.collectAsState()

    Scaffold (
        topBar = {
            CreditScoreTopAppBar(
                title = "Credit Score History",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(colorResource(R.color.background_purple))
        ){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        top = dimensionResource(R.dimen.padding_30dp)
                    )
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_30dp),
                            end = dimensionResource(R.dimen.padding_30dp),
                            top = dimensionResource(R.dimen.padding_30dp),
                            bottom = dimensionResource(R.dimen.padding_30dp)
                        )
                ){
                    Text(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = colorResource(R.color.light_purple),
                        text = "Credit Score History",
                    )
                }
//                CoroutineScope(Dispatchers.Main).launch {
//                    val recruiterEmail = "abc123@gmail.com"
//                    val transactions = getTransactionsByEmail(recruiterEmail)
//
//                    // Now you can work with the list of transactions
//                    transactions.forEach {
//                        Log.d("Main", "Transaction: $it")
//                    }
//                }

                LazyColumn(
                    modifier = Modifier
                ) {
                    items(transactions.value) { transaction ->
                        // Assuming you have a TransactionItem composable to display each transaction
                        TopUpHistoryRow(transaction)
                    }
                }
            }

        }
    }
}