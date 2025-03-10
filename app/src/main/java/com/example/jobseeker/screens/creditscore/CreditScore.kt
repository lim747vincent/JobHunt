package com.example.jobseeker.screens.creditscore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobseeker.R
import com.example.jobseeker.component.TopUpHistoryRow
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScoreModule(
    recruiterViewModel: RecruiterViewModel,
    rootNavController: NavController,
    onNavigateUp : () -> Unit,
    canNavigateBack: Boolean = true,
){
    LaunchedEffect(Unit) {
        recruiterViewModel.getCreditScoreAmount(recruiterViewModel.getRecruiterEmail)
        recruiterViewModel.fetchTransactions(recruiterViewModel.getRecruiterEmail)
    }

    val transactions = recruiterViewModel.transactions.collectAsState()

    Scaffold (
        topBar = {
            CreditScoreTopAppBar(
                title = "Credit Score",
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
                modifier = Modifier,
            ){
                Spacer(
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_150dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    Text(
                        text = "Your Credit Score: ",
                        modifier = Modifier,
                        style = MaterialTheme.typography.displaySmall,
                        color = colorResource(R.color.font_color)
                    )
                    Text(
                        text = recruiterViewModel.getRecruiterCreditScore,
                        modifier = Modifier,
                        style = MaterialTheme.typography.displaySmall,
                        color = colorResource(R.color.font_color)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(top = dimensionResource(R.dimen.padding_50dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = dimensionResource(R.dimen.padding_30dp)),

                    ){
                    Button(
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp),
                        onClick = {rootNavController.navigate(Graph.TopUpGraph)},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.btn_color)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ){
                        Text(
                            text = "Top Up",
                            modifier = Modifier,
                            color = colorResource(R.color.light_purple)
                        )
                    }
                }
                Spacer(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_50dp))
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(
                            start = dimensionResource(R.dimen.padding_30dp),
                            end = dimensionResource(R.dimen.padding_30dp)
                        ),
                    thickness = .5.dp
                )
                Spacer(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_50dp))
                )
                Row(
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_30dp))
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column(
                    ){
                        Text(
                            text = "Credit Score History",
                            color = colorResource(R.color.light_purple),
                            modifier = Modifier
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
                        Button(
                            modifier = Modifier
                                .height(35.dp)
                                .width(100.dp),
                            onClick = { rootNavController.navigate(Graph.ViewHistoryGraph) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp),
                        ){
                            Text(
                                text = "View All", color =
                                colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_30dp))
                )

                LazyColumn(
                    modifier = Modifier
                ) {
                    items(transactions.value.take(5)) { transaction ->
                        // Assuming you have a TransactionItem composable to display each transaction
                        TopUpHistoryRow(transaction)
                    }
                }
    }



        }
    }
}