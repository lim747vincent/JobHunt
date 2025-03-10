package com.example.jobseeker.viewModel

import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.jobseeker.model.CompanyUiState
import com.example.jobseeker.model.CreditScoreTransactionUiState
import com.example.jobseeker.model.JobApplicationUiState
import com.example.jobseeker.model.JobPostingUiState
import com.example.jobseeker.model.JobSeekerUiState
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.model.RecruiterNotificationUiState
import com.example.jobseeker.model.RecruiterUiState
import com.example.jobseeker.model.UserUiState
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.Graph
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class RecruiterViewModel : ViewModel() {

    var repeatPassword by mutableStateOf("")

    var recruiterLoginEmail by mutableStateOf("")

    var recruiterLoginPassword by mutableStateOf("")

    var getRecruiterEmail by mutableStateOf("")

    var getRecruiterPassword by mutableStateOf("")

    var getRecruiterFullName by mutableStateOf("")

    var getRecruiterCreditScore by mutableStateOf("")

    var newRecruiterCreditScore by mutableStateOf("")

    var isLoggedOut by mutableStateOf(false)

    var isSuccessful by mutableStateOf(false)

    var imageUri by mutableStateOf<Uri?>(null)

    var imageUrl by mutableStateOf("")

    var jobseekerId by mutableStateOf("")

    var resumeUrl by mutableStateOf("")

    private val _uiState_SelectedNotification = MutableStateFlow(RecruiterNotificationUiState())
    val uiState_SelectedNotification: StateFlow<RecruiterNotificationUiState> =
        _uiState_SelectedNotification.asStateFlow()

    private val _uiState_Profile = MutableStateFlow(RecruiterUiState())
    val uiState_Profile: StateFlow<RecruiterUiState> = _uiState_Profile.asStateFlow()

    private val _uiState_Recruiter = MutableStateFlow(RecruiterUiState())
    val uiState_Recruiter: StateFlow<RecruiterUiState> = _uiState_Recruiter.asStateFlow()

    private val _uiState_CreditScoreTransaction = MutableStateFlow(CreditScoreTransactionUiState())
    val uiState_CreditScoreTransaction: StateFlow<CreditScoreTransactionUiState> = _uiState_CreditScoreTransaction.asStateFlow()

    private val _uiState_Company = MutableStateFlow(CompanyUiState())
    val uiState_Company: StateFlow<CompanyUiState> = _uiState_Company.asStateFlow()

    private val _uiState_JobPost = MutableStateFlow(JobPostingUiState())
    val uiState_JobPost: StateFlow<JobPostingUiState> = _uiState_JobPost.asStateFlow()

    private val _transactions = MutableStateFlow<List<CreditScoreTransactionUiState>>(emptyList())
    val transactions = _transactions.asStateFlow()
    
    private val _companies = MutableStateFlow<List<CompanyUiState>>(emptyList())
    val companies = _companies.asStateFlow()

    private val _activeJobPost = MutableStateFlow<List<JobPostingUiState>>(emptyList())
    val activeJobPost = _activeJobPost.asStateFlow()

    private val _inactiveJobPost = MutableStateFlow<List<JobPostingUiState>>(emptyList())
    val inactiveJobPost = _inactiveJobPost.asStateFlow()

    private val _jobApplications = MutableStateFlow<List<JobApplicationUiState>>(emptyList())
    val jobApplications = _jobApplications.asStateFlow()

    private val _jobApplication = MutableStateFlow<JobApplicationUiState?>(null)
    val jobApplication: StateFlow<JobApplicationUiState?> = _jobApplication.asStateFlow()

    private val _uploadedResumeUrl = mutableStateOf("")
    val uploadedResumeUrl: State<String> get() = _uploadedResumeUrl

    private val _jobseeker = MutableStateFlow(JobSeekerUiState())
    val jobseeker: StateFlow<JobSeekerUiState> = _jobseeker.asStateFlow()

    private val _fetchedJobPost = MutableStateFlow<JobPostingUiState?>(null)
    val fetchedJobPost: StateFlow<JobPostingUiState?> = _fetchedJobPost.asStateFlow()

    val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    private val SESSION_DURATION = 7 * 24 * 60 * 60 * 1000 // 7 days in milliseconds

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${System.currentTimeMillis()}.jpg")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Once the image is successfully uploaded, retrieve the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    Log.d("Firebase", "Image uploaded successfully: $downloadUrl")
                    onSuccess(downloadUrl) // Return the download URL to the Composable
                }.addOnFailureListener { e ->
                    Log.d("Firebase", "Failed to get download URL: ${e.message}")
                    // Handle the failure to get the download URL
                }
            }
            .addOnFailureListener { e ->
                Log.d("Firebase", "Failed to upload image: ${e.message}")
                // Handle the failure to upload the image
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateAllExpiredJobPosts() = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Jobpost")

        try {
            // Get all job posts from Firestore
            val jobPostsSnapshot = fireStoreRef.get().await()

            // Get today's date
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            for (document in jobPostsSnapshot.documents) {
                // Get the job post data
                val jobPostId = document.id
                val jobPostEndDate = document.getString("jobpost_end_date")

                if (jobPostEndDate != null) {
                    // Parse the end date
                    val endDate = LocalDate.parse(jobPostEndDate, formatter)

                    // Check if the job post has expired
                    if (endDate.isBefore(today)) {
                        // Update job post status to "inactive"
                        fireStoreRef.document(jobPostId)
                            .update("jobpost_status", "Inactive")
                            .addOnSuccessListener {
                                Log.d("JobPost", "JobPost ID: $jobPostId status updated to inactive")
                            }
                            .addOnFailureListener { e ->
                                Log.d("JobPost", "Error updating status for ID: $jobPostId: ${e.message}")
                            }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("JobPost", "Error checking and updating job posts: ${e.message}")
        }
    }

    fun setJobPostId(jobPost_id : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobPost_id = jobPost_id
            )
        }
    }

    fun setJobPostCompanyAddress(jobPostCompanyAddress : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_company_address = jobPostCompanyAddress
            )
        }
    }

    fun setJobPostCompanyLogoImage(jobPostCompanyLogoImage : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_company_logo_image_filepath = jobPostCompanyLogoImage
            )
        }
    }

    fun setJobPostCompanyName(jobPostCompanyName: String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_company_name = jobPostCompanyName
            )
        }
    }

    fun setJobPostCompanyState(jobPostCompanyState : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_company_state = jobPostCompanyState
            )
        }
    }

    fun setJobPostCompanyDescription(jobPostDescription : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_description = jobPostDescription
            )
        }
    }

    fun setJobPostCompanyEmploymentType(jobPostCompanyEmploymentType : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_employment_type = jobPostCompanyEmploymentType
            )
        }
    }

    fun setJobPostCompanySize(jobPostCompanySize : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_company_size = jobPostCompanySize
            )
        }
    }

    fun setJobPostCompanyIndustry(jobPostCompanyIndustry : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_company_industry = jobPostCompanyIndustry
            )
        }
    }

    fun setJobPostEndDate(jobPostEndDate : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_end_date = jobPostEndDate
            )
        }
    }

    fun setJobPostSalaryEnd(jobPostSalaryEnd : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_salary_end = jobPostSalaryEnd.toDouble()
            )
        }
    }

    fun setJobPostSalaryStart(jobPostSalaryStart : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_salary_start = jobPostSalaryStart.toDouble()
            )
        }
    }

    fun setJobPostStartDate(jobPostStartDate : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_start_date = jobPostStartDate
            )
        }
    }

    fun setJobPostStatus(jobPostStatus: String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_status = jobPostStatus
            )
        }
    }

    fun setJobPostTitle(jobPostTitle : String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                jobpost_title = jobPostTitle
            )
        }
    }

    fun setJobPostRecruiterId(recruiterEmail: String){
        _uiState_JobPost.update { currentState ->
            currentState.copy(
                recruiter_id = recruiterEmail
            )
        }
    }

    fun setCompanyName(companyName : String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_name = companyName
            )
        }
    }

    fun setCompanyDescription(companyDescription : String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_description = companyDescription
            )
        }
    }

    fun setCompanyAddress(companyAddress : String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_address = companyAddress
            )
        }
    }

    fun setCompanyState(companyState: String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_state = companyState
            )
        }
    }

    fun setCompanySize(companySize : String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_size = companySize
            )
        }
    }

    fun setCompanyIndustry(companyIndustry : String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_industry = companyIndustry
            )
        }
    }

    fun setCompanyLogoImage(companyLogoImage : String){
        _uiState_Company.update { currentState ->
            currentState.copy(
                company_logo_image = companyLogoImage
            )
        }
    }

    fun setCreditScoreTransactionAmount(creditScoreTransactionAmount: String){
        _uiState_CreditScoreTransaction.update { currentState ->
            currentState.copy(
                credit_score_topup_amount = creditScoreTransactionAmount
            )
        }
    }

    fun setCreditScoreTransactionPaymentMethod(creditScorePaymentMethod: String){
        _uiState_CreditScoreTransaction.update { currentState ->
            currentState.copy(
                credit_score_payment_method = creditScorePaymentMethod
            )
        }
    }

    fun setCreditScoreTransactionDate(creditScoreTransactionDate: String){
        _uiState_CreditScoreTransaction.update { currentState ->
            currentState.copy(
                credit_score_transaction_date = creditScoreTransactionDate
            )
        }
    }

    fun setCreditScoreTransactionRecruiter(recruiterId: String){
        _uiState_CreditScoreTransaction.update { currentState ->
            currentState.copy(
                recruiter_id = getRecruiterEmail
            )
        }
    }

    fun setRecruiterFullName(recruiterName: String){
        _uiState_Recruiter.update{ currentState ->
            currentState.copy(
                recruiter_full_name = recruiterName
            )
        }
    }

    fun setRecruiterEmail(recruiterEmail: String){
        _uiState_Recruiter.update { currentState ->
            currentState.copy(
                recruiter_email = recruiterEmail
            )
        }
    }

    fun setRecruiterPassword(recruiterPassword: String) {
        _uiState_Recruiter.update { currentState ->
            currentState.copy(
                recruiter_password = recruiterPassword
            )
        }
    }

    fun isMatched(): Boolean{
        return _uiState_Recruiter.value.recruiter_password == repeatPassword
    }

    fun saveJobPost(
        jobPostData: JobPostingUiState,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("Jobpost")

        try{
            if(getRecruiterCreditScore.toDouble() < 10){
                Toast.makeText(context, "Please top up to create job posting", Toast.LENGTH_SHORT).show()
            }
            else{
                newRecruiterCreditScore = (getRecruiterCreditScore.toDouble() - 10).toString()
                updateRecruiterCreditScore(recruiterLoginEmail, newRecruiterCreditScore)
                fireStoreRef.add(jobPostData)
                    .addOnSuccessListener { documentReference ->
                        val jobPostId = documentReference.id

                        documentReference.update("jobPost_id", jobPostId)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Job Posted Successfully!", Toast.LENGTH_SHORT).show()
                            }
                    }
            }
        } catch(e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveCompany(
        companyData: CompanyUiState,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("Company")

        try{
            fireStoreRef.add(companyData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Added Company", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getJobApplicationById(jobApplicationId: String): JobApplicationUiState? {
        val fireStoreRef = Firebase.firestore

        return try {

            val documentSnapshot = fireStoreRef
                .collection("JobApplication")
                .document(jobApplicationId)
                .get().await()


            val jobApplicationData = documentSnapshot.toObject(JobApplicationUiState::class.java)

            if (jobApplicationData != null) {
                Log.d(TAG, "Job Application retrieved: $jobApplicationData")
                jobApplicationData
            } else {
                Log.d(TAG, "No job application found with ID: $jobApplicationId")
                null
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting job application: ${e.message}")
            null
        }
    }

    // Function to fetch a job application based on jobApplicationId
    fun fetchJobApplicationById(jobApplicationId: String) {
        viewModelScope.launch {
            val jobApplication = getJobApplicationById(jobApplicationId)
            _jobApplication.value = jobApplication // Assuming _jobApplication is the LiveData for a single job application
        }
    }

    suspend fun getJobPostById(jobPostId: String): JobPostingUiState? {
        val fireStoreRef = Firebase.firestore

        return try {

            val documentSnapshot = fireStoreRef
                .collection("Jobpost")
                .document(jobPostId)
                .get().await()


            val jobPostData = documentSnapshot.toObject(JobPostingUiState::class.java)

            if (jobPostData != null) {
                Log.d(TAG, "Job posting retrieved: $jobPostData")
                jobPostData
            } else {
                Log.d(TAG, "No job posting found with ID: $jobPostId")
                null
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting job posting: ${e.message}")
            null
        }
    }

    fun fetchJobPostById(jobPostId: String) {
        viewModelScope.launch {
            val jobPost = getJobPostById(jobPostId)
            _fetchedJobPost.value = jobPost
        }
    }

    suspend fun getJobSeekerById(jobSeekerId: String): JobSeekerUiState? {
        val fireStoreRef = Firebase.firestore

        return try {

            val documentSnapshot = fireStoreRef
                .collection("Jobseeker")
                .document(jobSeekerId)
                .get().await()


            val jobSeekerData = documentSnapshot.toObject(JobSeekerUiState::class.java)

            if (jobSeekerData != null) {
                Log.d(TAG, "Job seeker retrieved: $jobSeekerData")
                jobSeekerData
            } else {
                Log.d(TAG, "No job seeker found with ID: $jobSeekerData")
                null
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting job seeker: ${e.message}")
            null
        }
    }

    fun fetchJobSeekerById(jobSeekerId: String) {
        viewModelScope.launch {
            val jobSeeker = getJobSeekerById(jobSeekerId)
            if (jobSeeker != null) {
                _jobseeker.value = jobSeeker
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateJobApplicationStatus(jobApplicationId: String, newStatus: String, jobpostTitle: String, recruiterId: String, jobseekerId: String, jobpostId: String, context: android.content.Context) {
        val jobApplicationRef = Firebase.firestore.collection("JobApplication").document(jobApplicationId)
        jobApplicationRef.update("jobApplication_status", newStatus)
            .addOnSuccessListener {
                sendJobseekerNotification(jobApplicationId, jobpostTitle, recruiterId, jobseekerId, jobpostId, newStatus)
                Toast.makeText(context, "Successfully Updated Progress", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle failure
                Log.w(TAG, "Error updating document", e)
            }
    }

    suspend fun getUploadedResumeUrl(jobseekerId: String): String? {
        val fireStoreRef = Firebase.firestore

        return try {
            val documentSnapshot = fireStoreRef
                .collection("UploadedResume")
                .whereEqualTo("jobseeker_id", jobseekerId)
                .get()
                .await()

            if (!documentSnapshot.isEmpty) {
                val uploadedResumeData = documentSnapshot.documents.first().getString("uploaded_resume_file_url")
                Log.d(TAG, "Uploaded Resume URL retrieved: $uploadedResumeData")
                uploadedResumeData
            } else {
                Log.d(TAG, "No uploaded resume found for jobseeker ID: $jobseekerId")
                null
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting uploaded resume URL: ${e.message}")
            null
        }
    }

    // Function to fetch uploaded resume URL by jobseeker ID
    fun fetchUploadedResumeUrl(jobseekerId: String) {
        viewModelScope.launch {
            val uploadedResumeUrl = getUploadedResumeUrl(jobseekerId)
            _uploadedResumeUrl.value = uploadedResumeUrl ?: "" // Assuming _uploadedResumeUrl is the MutableState for the URL
        }
    }

    // Suspend function to get job applications based on recruiter email
    suspend fun getJobApplicationsByRecruiterEmail(recruiterEmail: String): List<JobApplicationUiState> {
        val fireStoreRef = Firebase.firestore

        return try {
            // Step 1: Query Jobpost collection to get all jobpost_ids with recruiter_id = recruiterEmail
            val jobPostSnapshot = fireStoreRef
                .collection("Jobpost")
                .whereEqualTo("recruiter_id", recruiterEmail)
                .get().await()

            // Extract jobpost_ids from the Jobpost documents
            val jobPostIds = jobPostSnapshot.documents.mapNotNull { it.id }

            if (jobPostIds.isNotEmpty()) {
                // Step 2: Query JobApplication collection where jobpost_id is in the list of retrieved jobpost_ids
                val jobApplicationSnapshot = fireStoreRef
                    .collection("JobApplication")
                    .whereIn("jobpost_id", jobPostIds)
                    .get().await()

                // Step 3: Convert JobApplication documents to JobApplicationUiState objects
                val jobApplicationList = mutableListOf<JobApplicationUiState>()

                for (document in jobApplicationSnapshot.documents) {
                    val jobApplicationData = document.toObject(JobApplicationUiState::class.java)
                    jobApplicationData?.let { jobApplicationList.add(it) }
                }

                Log.d(TAG, "Job Applications retrieved: $jobApplicationList")
                jobApplicationList  // Return the list of job applications

            } else {
                Log.d(TAG, "No job posts found for $recruiterEmail")
                emptyList()  // Return an empty list if no job posts are found
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting job applications: ${e.message}")
            emptyList()  // Return an empty list in case of error
        }
    }

    // Function to fetch job applications based on recruiter email
    fun fetchJobApplicationsByRecruiter(recruiterEmail: String) {
        viewModelScope.launch {
            val jobApplications = getJobApplicationsByRecruiterEmail(recruiterEmail)
            _jobApplications.value = jobApplications // Assuming _jobApplications is the LiveData for job applications
        }
    }

    suspend fun getActiveJobPostByEmail(recruiterEmail: String): List<JobPostingUiState> {
        val fireStoreRef = Firebase.firestore
            .collection("Jobpost")
            .whereEqualTo("recruiter_id", recruiterEmail)
            .whereEqualTo("jobpost_status", "Active")

        return try {
            // Execute the query and get matching documents
            val querySnapshot = fireStoreRef.get().await()

            if (!querySnapshot.isEmpty) {
                // Create a list to store the transaction data
                val jobPostingList = mutableListOf<JobPostingUiState>()

                for (document in querySnapshot.documents) {
                    // Convert each document to a CreditScoreTransactionUiState object
                    val jobPostingData = document.toObject(JobPostingUiState::class.java)
                    jobPostingData?.let { jobPostingList.add(it) }
                }
                Log.d(TAG, "Companies retrieved: $jobPostingList")
                jobPostingList  // Return the list of transactions
            } else {
                Log.d(TAG, "No companies found for $recruiterEmail")
                emptyList()  // Return an empty list if no transactions are found
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting companies: ${e.message}")
            emptyList()  // Return an empty list in case of error
        }
    }

    fun fetchActiveJobPost(recruiterEmail: String) {
        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {
            val activeJobPost = getActiveJobPostByEmail(recruiterEmail)
            _activeJobPost.value = activeJobPost
        }
    }

    suspend fun getInActiveJobPostByEmail(recruiterEmail: String): List<JobPostingUiState> {
        val fireStoreRef = Firebase.firestore
            .collection("Jobpost")
            .whereEqualTo("recruiter_id", recruiterEmail)
            .whereEqualTo("jobpost_status", "Inactive")

        return try {
            // Execute the query and get matching documents
            val querySnapshot = fireStoreRef.get().await()

            if (!querySnapshot.isEmpty) {
                // Create a list to store the transaction data
                val jobPostingList = mutableListOf<JobPostingUiState>()

                for (document in querySnapshot.documents) {
                    // Convert each document to a CreditScoreTransactionUiState object
                    val jobPostingData = document.toObject(JobPostingUiState::class.java)
                    jobPostingData?.let { jobPostingList.add(it) }
                }
                Log.d(TAG, "Companies retrieved: $jobPostingList")
                jobPostingList  // Return the list of transactions
            } else {
                Log.d(TAG, "No companies found for $recruiterEmail")
                emptyList()  // Return an empty list if no transactions are found
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting companies: ${e.message}")
            emptyList()  // Return an empty list in case of error
        }
    }

    fun fetchInActiveJobPost(recruiterEmail: String) {
        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {
            val inactiveJobPost = getInActiveJobPostByEmail(recruiterEmail)
            _inactiveJobPost.value = inactiveJobPost
        }
    }

    suspend fun getCompaniesByEmail(recruiterEmail: String): List<CompanyUiState> {
        val fireStoreRef = Firebase.firestore
            .collection("Company")
            .whereEqualTo("recruiter_id", recruiterEmail)

        return try {
            // Execute the query and get matching documents
            val querySnapshot = fireStoreRef.get().await()

            if (!querySnapshot.isEmpty) {
                // Create a list to store the transaction data
                val companyList = mutableListOf<CompanyUiState>()

                for (document in querySnapshot.documents) {
                    // Convert each document to a CreditScoreTransactionUiState object
                    val companyData = document.toObject(CompanyUiState::class.java)
                    companyData?.let { companyList.add(it) }
                }
                Log.d(TAG, "Companies retrieved: $companyList")
                companyList  // Return the list of transactions
            } else {
                Log.d(TAG, "No companies found for $recruiterEmail")
                emptyList()  // Return an empty list if no transactions are found
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting companies: ${e.message}")
            emptyList()  // Return an empty list in case of error
        }
    }

    fun fetchCompanies(recruiterEmail: String) {
        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {
            val companyList = getCompaniesByEmail(recruiterEmail)
            _companies.value = companyList
        }
    }

    fun deleteCompanyByName(
        companyName: String,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("Company")

        try {
            // Query Firestore to find the document with matching company_name
            fireStoreRef.whereEqualTo("company_name", companyName)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    // Check if any documents are found
                    if (!querySnapshot.isEmpty) {
                        // Loop through the documents and delete each one
                        for (document in querySnapshot.documents) {
                            fireStoreRef.document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    fetchCompanies(recruiterLoginEmail)
                                    Toast.makeText(context, "Successfully Deleted Company", Toast.LENGTH_SHORT).show()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Error deleting company: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // If no company is found
                        Toast.makeText(context, "No Company found with name: $companyName", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error finding company: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setRecruiterNotification(notification: RecruiterNotificationUiState) {
        _uiState_SelectedNotification.update { currentState ->
            currentState.copy(
                recruiter_notification_title = notification.recruiter_notification_title,
                recruiter_notification_date = notification.recruiter_notification_date,
                recruiter_notification_brief_text = notification.recruiter_notification_brief_text,
                recruiter_notification_detail_text = notification.recruiter_notification_detail_text,
                recruiter_notification_sender = notification.recruiter_notification_sender,
                recruiter_notification_isRead = notification.recruiter_notification_isRead,
                recruiter_id = notification.recruiter_id,
                jobpost_id = notification.jobpost_id
            )
        }
    }

    suspend fun getNotificationForRecruiter(recruiterId : String): List<RecruiterNotificationUiState> {
        val db = FirebaseFirestore.getInstance()
        val notificationList = mutableListOf<RecruiterNotificationUiState>()

        _isLoading.value = true

        withContext(Dispatchers.IO) {
            try {
                // Step 1: Query SavedJob collection for the given recruiter_id
                val jobNotificationSnapshot = db.collection("RecruiterNotification")
                    .whereEqualTo("recruiter_id", recruiterId)
                    .get()
                    .await()

                val savedJobs =
                    jobNotificationSnapshot.toObjects(RecruiterNotificationUiState::class.java)

                notificationList.addAll(savedJobs)

            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors that occur
            } finally {
                _isLoading.value = false // Hide loading
            }
        }

        return notificationList
    }

    fun updateRecruiterNotificationStatus(notification: RecruiterNotificationUiState) {
        val notificationId = notification.recruiterNotification_id
        val db = FirebaseFirestore.getInstance()

        val notificationRef = db.collection("RecruiterNotification")
            .document(notificationId)

        notificationRef.update("recruiter_notification_isRead", true)
            .addOnSuccessListener {
                Log.d("Firebase", "Notification ${notificationId} marked as read successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error updating notification: ", e)
            }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun saveData(
        recruiterData: RecruiterUiState,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("Recruiter")
            .document(recruiterData.recruiter_email)

        try {
            fireStoreRef.set(recruiterData)
                .addOnSuccessListener {
                    createCreditScoreForRecruiter(recruiterData.recruiter_email, context)
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun getTransactionsByEmail(recruiterEmail: String): List<CreditScoreTransactionUiState> {
        val fireStoreRef = Firebase.firestore
            .collection("CreditScoreTransaction")
            .whereEqualTo("recruiter_id", recruiterEmail)

        return try {
            // Execute the query and get matching documents
            val querySnapshot = fireStoreRef.get().await()

            if (!querySnapshot.isEmpty) {
                // Create a list to store the transaction data
                val transactionList = mutableListOf<CreditScoreTransactionUiState>()

                for (document in querySnapshot.documents) {
                    // Convert each document to a CreditScoreTransactionUiState object
                    val transactionData = document.toObject(CreditScoreTransactionUiState::class.java)
                    transactionData?.let { transactionList.add(it) }
                }
                Log.d(TAG, "Transactions retrieved: $transactionList")
                transactionList  // Return the list of transactions
            } else {
                Log.d(TAG, "No transactions found for $recruiterEmail")
                emptyList()  // Return an empty list if no transactions are found
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting transactions: ${e.message}")
            emptyList()  // Return an empty list in case of error
        }
    }

    fun fetchTransactions(recruiterEmail: String) {
        // Launch a coroutine in the viewModelScope
        viewModelScope.launch {
            val transactionList = getTransactionsByEmail(recruiterEmail)
            _transactions.value = transactionList
        }
    }

    fun updateRecruiterCreditScore(recruiterEmail: String, newCreditScore: String) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("CreditScore")
            .document(recruiterEmail)

        try {
            // Retrieve the document
            val documentSnapshot = fireStoreRef.get().await()

            if (documentSnapshot.exists()) {
                // Get the current credit score
                getRecruiterCreditScore = documentSnapshot.getString("credit_score_amount").toString()

                // Check if the document contains a valid credit score
                if (getRecruiterCreditScore.isNotEmpty()) {
                    Log.d(TAG, "Current Credit Score: $getRecruiterCreditScore")

                    // Now, update the credit score with the new value
                    fireStoreRef.update("credit_score_amount", newCreditScore)
                        .addOnSuccessListener {
                            Log.d(TAG, "Credit score successfully updated to: $newCreditScore")
                        }
                        .addOnFailureListener { e ->
                            Log.d(TAG, "Error updating credit score: ${e.message}")
                        }
                } else {
                    Log.d(TAG, "Recruiter not found")
                }
            } else {
                Log.d(TAG, "No Such Document")
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error getting or updating credit score: ${e.message}")
        }
    }

    fun saveCreditScoreTransaction(
        creditScoreTransactionData : CreditScoreTransactionUiState,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("CreditScoreTransaction")

        try{
            fireStoreRef.add(creditScoreTransactionData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Top Up", Toast.LENGTH_SHORT).show()
                }
        } catch(e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun saveDeductCreditScoreTransaction(
        creditScoreTransactionData : CreditScoreTransactionUiState,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch{

        val fireStoreRef = Firebase.firestore
            .collection("CreditScoreTransaction")

        try{
            fireStoreRef.add(creditScoreTransactionData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Deducted", Toast.LENGTH_SHORT).show()
                }
        } catch(e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getCreditScoreAmount(recruiterEmail: String) = CoroutineScope(Dispatchers.IO).launch{
        val fireStoreRef = Firebase.firestore
            .collection("CreditScore")
            .document(recruiterEmail)

        try {
            val documentSnapshot = fireStoreRef.get().await()

            if(documentSnapshot.exists()){
                getRecruiterCreditScore = documentSnapshot.getString("credit_score_amount").toString()

                if(getRecruiterCreditScore != ""){
                    Log.d(TAG, "Credit Score Amount: $getRecruiterCreditScore")
                }
                else{
                    Log.d(TAG, "Recruiter not found")
                }
            }
            else{
                Log.d(TAG, "No Such Document")
            }
        } catch(e: Exception){
            Log.d(TAG, "Error getting credit score amount: ${e.message}")
        }
    }

    private fun createCreditScoreForRecruiter(
        recruiterEmail: String,
        context: Context
    ) {
        val creditScoreData = hashMapOf(
            "credit_score_amount" to "0",  // Initial credit score
            "recruiter_id" to recruiterEmail
        )

        val fireStoreRef = Firebase.firestore
            .collection("CreditScore")
            .document(recruiterEmail)

        try{
            fireStoreRef.set(creditScoreData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Credit Score created", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getRecruiter(recruiterId: String) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Recruiter")
            .document(recruiterId)

        try {
            // Use await() to turn the Firestore task into a suspend function
            val documentSnapshot = fireStoreRef.get().await()

            // Check if the document exists
            if (documentSnapshot.exists()) {
                // Extract specific fields from the document (email and password)
                getRecruiterFullName = documentSnapshot.getString("recruiter_full_name").toString()
                getRecruiterEmail = documentSnapshot.getString("recruiter_email").toString()
                getRecruiterPassword = documentSnapshot.getString("recruiter_password").toString()

                if (getRecruiterEmail != "" && getRecruiterPassword != "") {
                    Log.d(TAG, "Email: $getRecruiterEmail, Password: $getRecruiterPassword")

                    // Optionally, you can use this data to update the ViewModel or perform login checks
                    // e.g., setProfile(RecruiterUiState(email = email, password = password))
                } else {
                    Log.d(TAG, "Email or Password field is missing")
                }
            } else {
                Log.d(TAG, "No such document")
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting recruiter: ${e.message}")
        }
    }

    fun isEmailRegistered(email: String, callback: (Boolean) -> Unit) {
        Firebase.firestore.collection("Recruiter")
            .document(email) // Use document ID as the email
            .get()
            .addOnSuccessListener { document ->
                // If the document exists, email is already registered
                callback(document.exists()) // Return true if the document exists, false otherwise
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error checking email", e)
                callback(false) // Return false in case of an error
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendJobseekerNotification(
        jobApplicationId: String,
        jobPostTitle: String,
        recruiterId: String,
        jobseekerId: String,
        jobPostId: String,
        status: String
    ) {
        val db = FirebaseFirestore.getInstance()

        // Create the notification details without the jobseekerNotification_id
        val notificationData = hashMapOf(
            "jobseeker_id" to jobseekerId,
            "jobseeker_notification_brief_text" to "Your application has been $status",
            "jobseeker_notification_date" to LocalDateTime.now().format(DateTimeFormatter.ofPattern("d/M/yyyy")),
            "jobseeker_notification_detail_text" to "Your job application for Job ${jobPostTitle} has been $status",
            "jobseeker_notification_isRead" to false,
            "jobseeker_notification_sender" to recruiterId,
            "jobseeker_notification_title" to "Application Status Updated",
            "jobpost_id" to jobPostId
        )

        // Add the notification to Firestore
        db.collection("JobseekerNotification")
            .add(notificationData)
            .addOnSuccessListener { documentReference ->
                // Retrieve document ID and set it as jobseekerNotification_id
                val notificationId = documentReference.id
                db.collection("JobseekerNotification")
                    .document(notificationId)
                    .update("jobseekerNotification_id", notificationId)
                    .addOnSuccessListener {
                        Log.d("Notification", "Notification sent and ID set successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Notification", "Error setting notification ID", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.w("Notification", "Error sending notification", e)
            }
    }

    fun loginRecruiter(
        email: String,
        password: String,
        context: Context,
        navController: NavController
    ) {
        if (email.isEmpty()) {
            Toast.makeText(context, "Please fill up the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Retrieve email from Firestore
        db.collection("Recruiter").document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedEmail = document.getString("recruiter_email") ?: ""
                    val storedPassword = document.getString("recruiter_password") ?: ""
                    getRecruiterEmail = document.getString("recruiter_email") ?: ""
                    getRecruiterFullName = document.getString("recruiter_full_name") ?: ""

                    // Validate email and password
                    if (email == storedEmail) {
                        if (password == storedPassword) {
                            // Successful login
                            Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Graph.RecruiterMainScreenGraph) {
                                popUpTo(AuthRouteScreen.StartAuth.route) { inclusive = true }
                            }
                        } else {
                            // Invalid password
                            Toast.makeText(context, "Invalid password!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Email not found
                        Toast.makeText(context, "Email not registered, proceed to sign up", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Document not found
                    Toast.makeText(context, "Email not registered, proceed to sign up", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                // Handle error during Firestore retrieval
                Toast.makeText(context, "Error retrieving email: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}