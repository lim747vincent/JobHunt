package com.example.jobseeker.navigations.graphs

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jobseeker.model.CreditScoreTransactionUiState
import com.example.jobseeker.model.JobPostingUiState
import com.example.jobseeker.navigations.Graph
import com.example.jobseeker.navigations.MainRouteScreen
import com.example.jobseeker.screens.applicant.ApplicantDetails
import com.example.jobseeker.screens.applicant.ApplicationModule
import com.example.jobseeker.screens.creditscore.CreditScoreHistory
import com.example.jobseeker.screens.creditscore.CreditScoreModule
import com.example.jobseeker.screens.creditscore.CreditScoreTopUpScreen
import com.example.jobseeker.screens.creditscore.CreditTopUpSuccessfully
import com.example.jobseeker.screens.jobDetail.JobPostingHistory
import com.example.jobseeker.screens.jobDetail.recruiterProfile.AddCompany
import com.example.jobseeker.screens.jobDetail.recruiterProfile.ViewMyCompany
import com.example.jobseeker.screens.jobPosting.JobPostingDescription
import com.example.jobseeker.screens.jobPosting.JobPostingEmploymentType
import com.example.jobseeker.screens.jobPosting.JobPostingFinal
import com.example.jobseeker.screens.jobPosting.JobPostingPhoto
import com.example.jobseeker.screens.jobPosting.JobPostingSalary
import com.example.jobseeker.screens.jobPosting.JobPostingScreen
import com.example.jobseeker.screens.jobPosting.JobPostingSuccessful
import com.example.jobseeker.screens.main.MainScreen
import com.example.jobseeker.screens.main.RecruiterMainScreen
import com.example.jobseeker.screens.recuiterProfile.RecuiterCompany
import com.example.jobseeker.utils.WindowSize
import com.example.jobseeker.viewModel.JobseekerViewModel
import com.example.jobseeker.viewModel.RecruiterViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created 28-02-2024 at 03:04 pm
 */

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun RootNavGraph(isAuth : Boolean, windowSize: WindowSize) {
    val rootNavController = rememberNavController()
    val jobseekerViewModel: JobseekerViewModel = viewModel()
    val recruiterViewModel: RecruiterViewModel = viewModel()
//    val notificationUiState by jobseekerViewModel.uiState_SelectedNotification.collectAsState()
    val context = LocalContext.current

    val jobPostUiState by recruiterViewModel.uiState_JobPost.collectAsState()

    NavHost(
        navController = rootNavController,
        route = Graph.RootGraph,
        startDestination = if (isAuth) {
            Graph.RecruiterMainScreenGraph
        } else {
            Graph.AuthGraph
        }
    ) {
        authNavGraph(rootNavController = rootNavController, jobseekerViewModel = jobseekerViewModel, recruiterViewModel = recruiterViewModel)
        composable(route = Graph.MainScreenGraph){
            MainScreen(rootNavHostController = rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize)
        }
        composable(route = Graph.RecruiterMainScreenGraph){
            RecruiterMainScreen(
                rootNavHostController = rootNavController,
                jobseekerViewModel = jobseekerViewModel,
                recruiterViewModel = recruiterViewModel,
                windowSize = windowSize
            )
        }
        notificationNavGraph(rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize)
        recruiterNotificationNavGraph(rootNavController, recruiterViewModel = recruiterViewModel, windowSize = windowSize)
        jobDetailNavGraph(rootNavController, jobseekerViewModel = jobseekerViewModel, windowSize = windowSize)

        composable(route = Graph.CreditScoreModule){
            CreditScoreModule(rootNavController = rootNavController, onNavigateUp = {rootNavController.navigateUp()}, recruiterViewModel = recruiterViewModel)
        }
        composable(route = Graph.ApplicationModule){
            ApplicationModule(rootNavController = rootNavController, recruiterViewModel = recruiterViewModel, windowSize = windowSize)
        }
        composable(route = Graph.JobPostingModule){
            JobPostingScreen(rootNavController = rootNavController)
        }
        composable(route = Graph.TopUpGraph){
            CreditScoreTopUpScreen(
                rootNavController = rootNavController,
                onCancelButtonClicked = { rootNavController.popBackStack()},
                onNavigateUp = { rootNavController.navigateUp()},
                onNextButtonClicked = {

                                            rootNavController.navigate(Graph.SuccessfulGraph)



                                      },
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.ViewHistoryGraph){
            CreditScoreHistory(
                onNavigateUp = { rootNavController.navigateUp()},
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.SuccessfulGraph){
            CreditTopUpSuccessfully(
                rootNavController = rootNavController,
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.MyCompanyGraph){
            RecuiterCompany(
                rootNavController = rootNavController
            )
        }
        composable(route = Graph.ViewMyCompanyGraph){
            ViewMyCompany(
                rootNavController = rootNavController,
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.AddNewCompanyGraph){
            AddCompany(
                rootNavController = rootNavController,
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.AddNewJobGraph){
            JobPostingDescription(
                rootNavController = rootNavController,
                onNextButtonClicked = {rootNavController.navigate(Graph.JobPostingEmploymentGraph)},
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.JobPostingEmploymentGraph){
            JobPostingEmploymentType(
                rootNavController = rootNavController,
                onNextButtonClicked = {rootNavController.navigate(Graph.JobPostingSalaryGraph)},
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.JobPostingSalaryGraph){
            JobPostingSalary(
                rootNavController = rootNavController,
                onNextButtonClicked = {rootNavController.navigate(Graph.JobPostingPhotoGraph)},
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.JobPostingPhotoGraph) {
            JobPostingPhoto(
                rootNavController = rootNavController,
                onNextButtonClicked = {
                    val startDate = LocalDateTime.now()
                    val startDatePattern = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    val formattedStartDate = startDate.format(startDatePattern)

                    val endDate = startDate.plusDays(7)
                    val formattedEndDate = endDate.format(startDatePattern)

                    recruiterViewModel.setJobPostStartDate(formattedStartDate)
                    recruiterViewModel.setJobPostEndDate(formattedEndDate)

                    rootNavController.navigate(Graph.JobPostingFinalGraph)
                                      },
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.JobPostingFinalGraph) {
            JobPostingFinal(
                rootNavController = rootNavController,
                onPublishButtonClicked = {

                    if(recruiterViewModel.getRecruiterCreditScore.toDouble() < 10){
                        Toast.makeText(context, "Please top up to create new job posting.", Toast.LENGTH_SHORT).show()
                        return@JobPostingFinal
                    }
                    else{
                        val jobPostData = JobPostingUiState(
                            jobpost_title = jobPostUiState.jobpost_title,
                            jobpost_status = "Active",
                            jobpost_company_state = jobPostUiState.jobpost_company_state,
                            jobpost_description = jobPostUiState.jobpost_description,
                            jobpost_salary_start = jobPostUiState.jobpost_salary_start,
                            jobpost_start_date = jobPostUiState.jobpost_start_date,
                            jobpost_salary_end = jobPostUiState.jobpost_salary_end,
                            jobpost_end_date = jobPostUiState.jobpost_end_date,
                            jobpost_company_industry = jobPostUiState.jobpost_company_industry,
                            jobpost_company_size = jobPostUiState.jobpost_company_size,
                            jobpost_company_name = jobPostUiState.jobpost_company_name,
                            jobpost_employment_type = jobPostUiState.jobpost_employment_type,
                            jobpost_company_address = jobPostUiState.jobpost_company_address,
                            jobpost_company_logo_image_filepath = jobPostUiState.jobpost_company_logo_image_filepath,
                            recruiter_id = jobPostUiState.recruiter_id
                        )
                        val creditScoreTransactionData = CreditScoreTransactionUiState(
                            credit_score_transaction_title = "Job Posting",
                            credit_score_topup_amount = "-10",
                            credit_score_payment_method = "Credit Score",
                            credit_score_transaction_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
                            recruiter_id = jobPostUiState.recruiter_id
                        )
                        recruiterViewModel.saveDeductCreditScoreTransaction(creditScoreTransactionData, context)
                        recruiterViewModel.saveJobPost(jobPostData, context)
                        rootNavController.navigate(Graph.JobPostingSuccessfulGraph)
                    }
                    },
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.JobPostingHistoryGraph){
            JobPostingHistory(
                rootNavController = rootNavController,
                recruiterViewModel = recruiterViewModel
            )
        }
        composable(route = Graph.JobPostingSuccessfulGraph){
            JobPostingSuccessful(
                rootNavController = rootNavController, recruiterViewModel = recruiterViewModel
            )
        }
        composable(
            route = "${Graph.ApplicantDetailsGraph}/{jobApplicationId}",
            arguments = listOf(navArgument("jobApplicationId") { type = NavType.StringType })
        ){ backStackEntry ->
            val jobApplicationId = backStackEntry.arguments?.getString("jobApplicationId")
            jobApplicationId?.let {
                ApplicantDetails(
                    rootNavController = rootNavController,
                    recruiterViewModel = recruiterViewModel,
                    jobApplicationId = it
                    )
            }
        }
    }
}