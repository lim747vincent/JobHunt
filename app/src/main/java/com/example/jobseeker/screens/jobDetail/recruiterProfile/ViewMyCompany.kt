package com.example.jobseeker.screens.jobDetail.recruiterProfile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.component.CompanyCardList
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.viewModel.RecruiterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMyCompany(rootNavController: NavController, recruiterViewModel: RecruiterViewModel) {

    LaunchedEffect(Unit) {
        recruiterViewModel.fetchCompanies(recruiterViewModel.recruiterLoginEmail)
    }

    val companies = recruiterViewModel.companies.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = colorResource(id = R.color.top_bar_color)
                ),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    ) {
                        Text(
                            text = "View My Companies",
                            color = colorResource(id = R.color.page_title_color),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        rootNavController.navigateUp()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .background(colorResource(id = R.color.background_purple))
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
            ){
                items(companies.value) { company ->
                    CompanyCardList(company, recruiterViewModel)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ViewMyCompanyPreview() {
    val navController = rememberNavController()
    ViewMyCompany(rootNavController = navController, recruiterViewModel = viewModel())
}