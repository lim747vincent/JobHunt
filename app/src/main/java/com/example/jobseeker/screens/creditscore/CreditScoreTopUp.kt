package com.example.jobseeker.screens.creditscore

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jobseeker.R
import com.example.jobseeker.model.CreditScoreTransactionUiState
import com.example.jobseeker.viewModel.RecruiterViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScoreTopUpScreen(
    rootNavController: NavController,
    onCancelButtonClicked: () -> Unit,
    onNextButtonClicked: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    recruiterViewModel: RecruiterViewModel
){

    val creditScoreTransactionUiState by recruiterViewModel.uiState_CreditScoreTransaction.collectAsState()

    val context = LocalContext.current

    val pattern = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
    val dateTime = LocalDateTime.now()
    val formattedDateTime = dateTime.format(pattern)

    Scaffold (
        topBar = {
            CreditScoreTopAppBar(
                title = "Top Up Credit Score",
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ){ innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(color = colorResource(R.color.background_purple)),
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ){
                Spacer(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_50dp))
                )

                EditNumberField(
                    value = creditScoreTransactionUiState.credit_score_topup_amount,
                    onValueChange = {
                        recruiterViewModel.setCreditScoreTransactionAmount(it)
                    },
                    modifier = Modifier
                        .padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 15.dp)
                        .fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_30dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_30dp),
                            end = dimensionResource(R.dimen.padding_30dp)
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionAmount("10")
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "10",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionAmount("20")
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "20",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionAmount("30")
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "30",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_30dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_30dp),
                            end = dimensionResource(R.dimen.padding_30dp)
                        )
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionAmount("50")
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "50",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionAmount("100")
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "100",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionAmount("500")
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "500",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_15dp))
                )
                Row(
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_50dp))
                ){
                    Text(
                        text = "Select Preferred Payments Method: ",
                        color = colorResource(R.color.font_color)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_15dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_30dp),
                            end = dimensionResource(R.dimen.padding_30dp)
                        )
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionPaymentMethod("TNG eWallet")
                                recruiterViewModel.setCreditScoreTransactionDate(formattedDateTime)
                                recruiterViewModel.setCreditScoreTransactionRecruiter(recruiterViewModel.recruiterLoginEmail)
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Image(
                                painter = painterResource(R.drawable.touch__n_go_ewallet_svg),
                                contentDescription = null
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionPaymentMethod("Online Banking")
                                recruiterViewModel.setCreditScoreTransactionDate(formattedDateTime)
                                recruiterViewModel.setCreditScoreTransactionRecruiter(recruiterViewModel.recruiterLoginEmail)
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Image(
                                painter = painterResource(R.drawable.onlinebankinglogo),
                                contentDescription = null
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                recruiterViewModel.setCreditScoreTransactionPaymentMethod("Grab Pay")
                                recruiterViewModel.setCreditScoreTransactionDate(formattedDateTime)
                                recruiterViewModel.setCreditScoreTransactionRecruiter(recruiterViewModel.recruiterLoginEmail)
                            },
                            modifier = Modifier
                                .height(75.dp)
                                .width(90.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = colorResource(R.color.btn_color)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Image(
                                painter = painterResource(R.drawable.grabpay_final_logo_rgb_white_stackedversion_01),
                                contentDescription = null
                            )
                        }
                    }
                }
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_15dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = dimensionResource(R.dimen.padding_50dp),
                        )
                ){
                    Text(
                        text = "You have selected: ${creditScoreTransactionUiState.credit_score_payment_method}",
                        color = colorResource(R.color.font_color),
                        fontSize = 16.sp
                    )
                }
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_15dp))
                )
                Spacer(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.padding_80dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = { onCancelButtonClicked() },
                            modifier = Modifier
                                .width(160.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.btn_color)),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "Cancel",
                                fontSize = 24.sp,
                                color = colorResource(id = R.color.light_purple)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = dimensionResource(R.dimen.padding_30dp)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Button(
                            onClick = {
                                val creditScoreTransactionData = CreditScoreTransactionUiState(
                                    credit_score_transaction_title = "Top Up",
                                    credit_score_topup_amount = "+${creditScoreTransactionUiState.credit_score_topup_amount}",
                                    credit_score_payment_method = creditScoreTransactionUiState.credit_score_payment_method,
                                    credit_score_transaction_date = creditScoreTransactionUiState.credit_score_transaction_date,
                                    recruiter_id = creditScoreTransactionUiState.recruiter_id
                                )
                                recruiterViewModel.newRecruiterCreditScore = (recruiterViewModel.getRecruiterCreditScore.toDouble() + creditScoreTransactionUiState.credit_score_topup_amount.toDouble()).toString()

                                //viewModel.insertTransaction()
                                if(creditScoreTransactionUiState.credit_score_topup_amount.isBlank() || creditScoreTransactionUiState.credit_score_payment_method.isBlank() || creditScoreTransactionUiState.credit_score_transaction_date.isBlank()){
                                    recruiterViewModel.isSuccessful = false
                                }
                                else{
                                    recruiterViewModel.isSuccessful = true
                                }

                                if(recruiterViewModel.isSuccessful){
                                    recruiterViewModel.saveCreditScoreTransaction(creditScoreTransactionData = creditScoreTransactionData, context)
                                    recruiterViewModel.updateRecruiterCreditScore(recruiterEmail = recruiterViewModel.getRecruiterEmail, recruiterViewModel.newRecruiterCreditScore)
                                    onNextButtonClicked()
                                }
                                else{
                                    Toast.makeText(context, "Please ensure that payment amount and method has been selected.", Toast.LENGTH_SHORT).show()
                                }

                            },
                            modifier = Modifier
                                .width(160.dp)
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(colorResource(R.color.btn_color)),
                            shape = RoundedCornerShape(12.dp)
                        ){
                            Text(
                                text = "Proceed",
                                color = colorResource(id = R.color.light_purple),
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun EditNumberField(
    value : String,
    onValueChange: (String) -> Unit,
    modifier: Modifier){
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle.Default.copy(
            fontSize = 24.sp,
            color = colorResource(R.color.font_color)
        ),
        label = {
            Text(
                text = stringResource(R.string.enter_amount),
                color = colorResource(R.color.font_color),
            )
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number),
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.btn_color),
            focusedContainerColor = colorResource(R.color.btn_color)),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreditScoreTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    navigateUp: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }
            }
        }
    )
}