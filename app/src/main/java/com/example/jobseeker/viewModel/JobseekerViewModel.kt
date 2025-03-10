package com.example.jobseeker.viewModel


import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Build
import android.service.autofill.UserData
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.jobseeker.model.JobApplicationUiState
import com.example.jobseeker.model.JobFilteringFormUiState
import com.example.jobseeker.model.JobListingUiState
import com.example.jobseeker.model.JobseekerNotificationUiState
import com.example.jobseeker.model.RecruiterNotificationUiState
import com.example.jobseeker.model.RecruiterUiState
import com.example.jobseeker.model.SavedJobUiState
import com.example.jobseeker.model.UploadedResumeUiState
import com.example.jobseeker.model.UserUiState
import com.example.jobseeker.navigations.AuthRouteScreen
import com.example.jobseeker.navigations.Graph
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.UUID

class JobseekerViewModel : ViewModel() {
    var repeatPassword by mutableStateOf("")

    var email by mutableStateOf("")

    var password by mutableStateOf("")

    var getJobseekerEmail by mutableStateOf("")

    var getJobseekerPassword by mutableStateOf("")

    var getJobseekerFullName by mutableStateOf("")

    private val _uiState_Profile = MutableStateFlow(UserUiState())
    val uiState_Profile: StateFlow<UserUiState> = _uiState_Profile.asStateFlow()

    private val _uiState_SelectedNotification = MutableStateFlow(JobseekerNotificationUiState())
    val uiState_SelectedNotification: StateFlow<JobseekerNotificationUiState> =
        _uiState_SelectedNotification.asStateFlow()

    private val _uiState_SelectedJob = MutableStateFlow(JobListingUiState())
    val uiState_SelectedJob: StateFlow<JobListingUiState> = _uiState_SelectedJob.asStateFlow()

    private val _uiState_JobFilteringForm = MutableStateFlow(JobFilteringFormUiState())
    val uiState_JobFilteringForm: StateFlow<JobFilteringFormUiState> =
        _uiState_JobFilteringForm.asStateFlow()

    private val _uiState_Resume = MutableStateFlow(UploadedResumeUiState())
    val uiState_Resume: StateFlow<UploadedResumeUiState> = _uiState_Resume.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _uiState_Jobseeker = MutableStateFlow(UserUiState())
    val uiState_Jobseeker: StateFlow<UserUiState> = _uiState_Jobseeker.asStateFlow()

    val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun resetViewModel() {
        repeatPassword = ""
        email = ""
        password = ""
        getJobseekerEmail = ""
        getJobseekerPassword = ""
        getJobseekerFullName = ""
        _uiState_Jobseeker.value = UserUiState()
        _uiState_Profile.value = UserUiState()  // Reset user profile state
        _uiState_SelectedNotification.value =
            JobseekerNotificationUiState()  // Reset selected notification state
        _uiState_SelectedJob.value = JobListingUiState()  // Reset selected job state
        _uiState_JobFilteringForm.value =
            JobFilteringFormUiState()  // Reset job filtering form state
        _uiState_Resume.value = UploadedResumeUiState()  // Reset uploaded resume state
        _isLoading.value = true  // Reset loading state to indicate the app is ready
        email = ""
        password = ""
    }

    fun setIsLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    fun updateJobTitle(title: String) {
        _uiState_JobFilteringForm.value = _uiState_JobFilteringForm.value.copy(jobTitle = title)
    }

    fun updateSelectedEmploymentType(employmentType: String) {
        _uiState_JobFilteringForm.value =
            _uiState_JobFilteringForm.value.copy(selectedEmploymentType = employmentType)
    }

    fun updateSelectedLocation(location: String) {
        _uiState_JobFilteringForm.value =
            _uiState_JobFilteringForm.value.copy(selectedLocation = location)
    }

    fun updateSalaryRange(range: ClosedFloatingPointRange<Float>) {
        _uiState_JobFilteringForm.value = _uiState_JobFilteringForm.value.copy(salaryRange = range)
    }


    fun updateJobseekerNotificationStatus(notification: JobseekerNotificationUiState) {
        val notificationId = notification.jobseekerNotification_id
        val db = FirebaseFirestore.getInstance()

        val notificationRef = db.collection("JobseekerNotification")
            .document(notificationId)

        notificationRef.update("jobseeker_notification_isRead", true)
            .addOnSuccessListener {
                Log.d("Firebase", "Notification ${notificationId} marked as read successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Error updating notification: ", e)
            }
    }

    fun setJobseekerNotification(notification: JobseekerNotificationUiState) {
        _uiState_SelectedNotification.update { currentState ->
            currentState.copy(
                jobseeker_notification_title = notification.jobseeker_notification_title,
                jobseeker_notification_date = notification.jobseeker_notification_date,
                jobseeker_notification_brief_text = notification.jobseeker_notification_brief_text,
                jobseeker_notification_detail_text = notification.jobseeker_notification_detail_text,
                jobseeker_notification_sender = notification.jobseeker_notification_sender,
                jobseeker_notification_isRead = notification.jobseeker_notification_isRead,
                jobseeker_id = notification.jobseeker_id,
                jobpost_id =  notification.jobpost_id
            )
        }
    }

    fun setJobseekerJob(job: JobListingUiState) {
        _uiState_SelectedJob.update { currentState ->
            currentState.copy(
                jobPost_id = job.jobPost_id,
                jobpost_company_address = job.jobpost_company_address,
                jobpost_company_logo_image_filepath = job.jobpost_company_logo_image_filepath,
                jobpost_company_name = job.jobpost_company_name,
                jobpost_company_state = job.jobpost_company_state,
                jobpost_description = job.jobpost_description,
                jobpost_employment_type = job.jobpost_employment_type,
                jobpost_end_date = job.jobpost_end_date,
                jobpost_salary_end = job.jobpost_salary_end,
                jobpost_salary_start = job.jobpost_salary_start,
                jobpost_start_date = job.jobpost_start_date,
                jobpost_status = job.jobpost_status,
                jobpost_title = job.jobpost_title,
                jobApplication_status = job.jobApplication_status,
                recruiter_id = job.recruiter_id,
                isFavor = job.isFavor
            )
        }
    }


    fun setProfile(user: UserUiState) {
        _uiState_Profile.update { currentState ->
            currentState.copy(
                jobseeker_full_name = user.jobseeker_full_name,
                jobseeker_email = user.jobseeker_email,
                jobseeker_password = user.jobseeker_password
            )
        }
    }


    suspend fun addAppliedJob(jobseekerId: String, jobpostId: String) {
        val db = FirebaseFirestore.getInstance()

        // Create a new SavedJobUiState object
        val appliedJob = JobApplicationUiState(
            jobseeker_id = jobseekerId,
            jobpost_id = jobpostId,
            jobApplication_status = "Processing"
        )

        withContext(Dispatchers.IO) {
            try {
                // Use the add() method to save to the Firestore database
                val documentReference = db.collection("JobApplication")
                    .add(appliedJob)
                    .await() // Await the completion of the operation

                val generatedId = documentReference.id

                db.collection("JobApplication")
                    .document(generatedId)
                    .update("jobApplication_id", generatedId) // Update the field
                    .await() // Await the update operation

                Log.d("AddAppliedjob", "Applied job added with ID: ${documentReference.id}")
            } catch (e: Exception) {
                Log.e("AddAppliedjob", "Error adding Applied job: ${e.message}", e)
                e.printStackTrace()
                // Handle any errors that occur
            }
        }
    }


    fun setResume(resume: UploadedResumeUiState) {
        _uiState_Resume.update { currentState ->
            currentState.copy(
                jobseeker_id = resume.jobseeker_id,
                uploaded_resume_file_name = resume.uploaded_resume_file_name,
                uploaded_resume_file_url = resume.uploaded_resume_file_url
            )
        }
    }

    fun getResume(jobseekerId: String) = CoroutineScope(Dispatchers.IO).launch {
        val db = FirebaseFirestore.getInstance()

        withContext(Dispatchers.IO) {
            try {
                // Step 1: Query the SavedJob collection to find the document with matching jobseeker_id and jobpost_id
                val resumeSnapshot = db.collection("UploadedResume")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                // Step 2: If the document exists, delete it
                if (!resumeSnapshot.isEmpty) {
                    for (document in resumeSnapshot.documents) {
                        // Convert the document data to an UploadedResumeUiState object
                        val resumeData = document.toObject(UploadedResumeUiState::class.java)

                        resumeData?.let {
                            // Update the ViewModel's state with the resume data
                            setResume(it)
                            Log.d("Resume", "Data: $it")
                        }
                    }
                } else {
                    Log.d("Resume", "No resume found")
                }
            } catch (e: Exception) {
                Log.e("Resume", "Error: ${e.message}", e)
                e.printStackTrace()
                // Handle any errors that occur
            }
        }
    }


    fun updateUploadedResume(jobseekerId: String, fileName: String, fileUrl: String) {
        val db = FirebaseFirestore.getInstance()

        val uploadedResume = UploadedResumeUiState(
            jobseeker_id = jobseekerId,
            uploaded_resume_file_name = fileName,
            uploaded_resume_file_url = fileUrl
        )

        db.collection("UploadedResume")
            .document()
            .set(uploadedResume)
            .addOnSuccessListener {

                setResume(uploadedResume)
                Log.d("UpdateResume", "Uploaded resume record updated successfully.")
            }
            .addOnFailureListener { exception ->
                Log.e(
                    "UpdateResume",
                    "Error updating resume record: ${exception.message}",
                    exception
                )
            }

    }

    fun deleteResumeFile(jobseekerId: String, fileUrl: String, context: Context) {
        val db = FirebaseFirestore.getInstance()

        val filePath = fileUrl.substringAfter(".com/o/").substringBefore("?alt")

        // Decode the file path
        val decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString())

        // Create a reference to the Firebase Storage instance
        val storageRef = FirebaseStorage.getInstance().reference

        // Create a reference to the file to delete
        val fileRef = storageRef.child(decodedFilePath)

        Log.d("fileRef", "fileRef: $fileRef")


        // Step 1: Delete the file from Firebase Storage
        fileRef.delete().addOnSuccessListener {
            // File successfully deleted from Firebase Storage
            Log.d("DeleteResume", "File deleted successfully")

            // Step 2: Delete the record from Firestore
            db.collection("UploadedResume")
                .whereEqualTo("jobseeker_id", jobseekerId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        document.reference.delete().addOnSuccessListener {

                            setResume(UploadedResumeUiState())

                            Toast.makeText(
                                context,
                                "Successfully delete resume",
                                Toast.LENGTH_SHORT
                            ).show()

                            Log.d("DeleteResume", "Resume record deleted successfully")
                        }.addOnFailureListener { e ->
                            Log.e("DeleteResume", "Error deleting resume record: ${e.message}", e)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("DeleteResume", "Error finding resume record: ${e.message}", e)
                }

        }.addOnFailureListener { e ->
            Log.e("DeleteResume", "Error deleting file: ${e.message}", e)
        }
    }


    suspend fun uploadPdfToFirebase(uri: Uri, jobseekerId: String, context: Context) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference

        // Create a reference to the PDF file with a unique name
        val pdfRef = storageRef.child("pdfs/${System.currentTimeMillis()}")

        val fileName = uri.lastPathSegment ?: "default_filename.pdf"

        try {
            // Start the upload and await completion
            pdfRef.putFile(uri)
                .await() // Ensure you have the appropriate Firebase libraries to use `await()`

            pdfRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                Log.d("UploadPDF", "PDF uploaded successfully as: ${fileName}")
                // Update the UploadedResume record in Firestore
                updateUploadedResume(jobseekerId, fileName, downloadUrl.toString())

                Toast.makeText(context, "Successfully uploaded", Toast.LENGTH_SHORT).show()
            }

            // Handle successful upload
            Log.d("UploadPDF", "PDF uploaded successfully: ${uri.lastPathSegment}")
        } catch (exception: Exception) {
            // Handle unsuccessful uploads
            Log.e("UploadPDF", "Error uploading PDF: ${exception.message}", exception)
        }
    }


    suspend fun removeSavedJob(jobseekerId: String, jobpostId: String) {
        val db = FirebaseFirestore.getInstance()

        withContext(Dispatchers.IO) {
            try {
                // Step 1: Query the SavedJob collection to find the document with matching jobseeker_id and jobpost_id
                val savedJobSnapshot = db.collection("SavedJob")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .whereEqualTo("jobpost_id", jobpostId)
                    .get()
                    .await()

                // Step 2: If the document exists, delete it
                if (!savedJobSnapshot.isEmpty) {
                    val savedJobDocument =
                        savedJobSnapshot.documents[0] // Assuming there's only one match

                    db.collection("SavedJob")
                        .document(savedJobDocument.id)
                        .delete()
                        .await() // Await the delete operation

                    Log.d("RemoveSavedJob", "Saved job removed with ID: ${savedJobDocument.id}")
                } else {
                    Log.d("RemoveSavedJob", "No matching saved job found")
                }
            } catch (e: Exception) {
                Log.e("RemoveSavedJob", "Error removing saved job: ${e.message}", e)
                e.printStackTrace()
                // Handle any errors that occur
            }
        }
    }


    suspend fun addSavedJob(jobseekerId: String, jobpostId: String) {
        val db = FirebaseFirestore.getInstance()

        // Create a new SavedJobUiState object
        val savedJob = SavedJobUiState(
            jobseeker_id = jobseekerId,
            jobpost_id = jobpostId
        )

        withContext(Dispatchers.IO) {
            try {
                // Use the add() method to save to the Firestore database
                val documentReference = db.collection("SavedJob")
                    .add(savedJob)
                    .await() // Await the completion of the operation

                val generatedId = documentReference.id

                db.collection("SavedJob")
                    .document(generatedId)
                    .update("savedJob_id", generatedId) // Update the field
                    .await() // Await the update operation

                Log.d("AddSavedJob", "Saved job added with ID: ${documentReference.id}")
            } catch (e: Exception) {
                Log.e("AddSavedJob", "Error adding saved job: ${e.message}", e)
                e.printStackTrace()
                // Handle any errors that occur
            }
        }
    }


    fun getJobseeker(jobseekerId: String) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Jobseeker")
            .document(jobseekerId)

        _isLoading.value = true

        try {
            // Use await() to turn the Firestore task into a suspend function
            val documentSnapshot = fireStoreRef.get().await()

            // Check if the document exists
            if (documentSnapshot.exists()) {
                // Convert the document data to a UserUiState object
                val userData = documentSnapshot.toObject<UserUiState>()!!

                // Update the ViewModel's profile data
                setProfile(userData)

                Log.d(TAG, "Data: ${documentSnapshot.data}")


                true
            } else {
                Log.d(TAG, "No such document")
                false
            }

        } catch (e: Exception) {
            Log.d(TAG, "Error getting jobseeker: ${e.message}")
        } finally {
            _isLoading.value = false // Hide loading
        }
    }

    suspend fun searchJobPosts(jobseekerId: String): List<JobListingUiState> {
        _isLoading.value = true

        val db = FirebaseFirestore.getInstance()

        // Get the current filtering values
        val filterState = _uiState_JobFilteringForm.value
        val filteredJobPosts = mutableListOf<JobListingUiState>()

        try {
            var query: Query = db.collection("Jobpost")

            if (filterState.jobTitle.isNotEmpty()) {
                Log.d("FilterState", "filterState.jobTitle.isNotEmpty()")

                // Prefix search for job title
                val jobTitleStart = filterState.jobTitle
                val jobTitleEnd =
                    jobTitleStart + '\uf8ff'  // Firestore's range operator (end character for string matching)

                query = query
                    .whereGreaterThanOrEqualTo("jobpost_title", jobTitleStart)
                    .whereLessThanOrEqualTo("jobpost_title", jobTitleEnd).orderBy("jobpost_title")
            }

            // If the user selected an employment type, filter by employment type
            if (filterState.selectedEmploymentType != "Select Option") {
                Log.d("FilterState", "filterState.selectedEmploymentType.isNotEmpty()")

                query = query.whereEqualTo(
                    "jobpost_employment_type",
                    filterState.selectedEmploymentType
                )
            }

            // If the user selected a location, filter by location
            if (filterState.selectedLocation != "Select Option") {
                Log.d("FilterState", "filterState.selectedLocation.isNotEmpty()")

                query = query.whereEqualTo("jobpost_company_state", filterState.selectedLocation)
            }

            // For salary range, we filter only if the range is different from the default
            if (filterState.salaryRange.start > 0f || filterState.salaryRange.endInclusive < 10000f) {
                Log.d("FilterState", "filterState.salaryRange.endInclusive < 10000f")

                query = query.whereGreaterThanOrEqualTo(
                    "jobpost_salary_start",
                    filterState.salaryRange.start.toDouble()
                )
                    .whereLessThanOrEqualTo(
                        "jobpost_salary_end",
                        filterState.salaryRange.endInclusive.toDouble()
                    ).orderBy("jobpost_salary_start")

            }

            query = query.whereEqualTo("jobpost_status", "Active")


            val jobPostsSnapshot = query.get().await()

            if (!jobPostsSnapshot.isEmpty) {
//                val jobPosts = jobPostsSnapshot.toObjects(JobListingUiState::class.java)

                val savedJobsSnapshot = db.collection("SavedJob")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val savedJobIds =
                    savedJobsSnapshot.documents.map { it.getString("jobpost_id") }.filterNotNull()

                val jobApplicationsSnapshot = db.collection("JobApplication")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val jobApplicationMap = jobApplicationsSnapshot.documents.associate { doc ->
                    val jobpostId = doc.getString("jobpost_id") ?: ""
                    jobpostId to doc.getString("jobApplication_status")
                }



                jobPostsSnapshot.documents.forEach { doc ->
                    val job = doc.toObject(JobListingUiState::class.java)
                    job?.let {
                        it.isFavor = savedJobIds.contains(doc.id)
                        it.jobApplication_status = jobApplicationMap[doc.id]
                            ?: "Not Applied" // Default status if not applied

                        filteredJobPosts.add(it)

                        Log.d("JobPost", "Job ID: ${doc.id}, isFavor: ${it.isFavor}")
                    }
                }


            } else {
                Log.d("FirestoreQuery", "No matching job posts found.")
            }


        } catch (e: Exception) {
            e.printStackTrace()
            // Handle errors here
        } finally {
            _isLoading.value = false
        }

        return filteredJobPosts
    }

    suspend fun getAllJobPost(jobseekerId: String): List<JobListingUiState> {
        val db = FirebaseFirestore.getInstance()
        val jobPosts = mutableListOf<JobListingUiState>()

//        _isLoading.value = true

        withContext(Dispatchers.IO) {
            try {
                val jobPostsSnapshot = db.collection("Jobpost")
                    .whereEqualTo("jobpost_status", "Active") // Filter active job posts
                    .orderBy(
                        "jobpost_start_date",
                        Query.Direction.DESCENDING
                    ) // Sort by latest jobs
                    .get()
                    .await()

                val savedJobsSnapshot = db.collection("SavedJob")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

//                savedJobsSnapshot.documents.forEach { document ->
//                    Log.d("SavedJob", "Document ID: ${document.id}, Data: ${document.data}")
//                }

                // Extract jobpost_id from saved jobs
                val savedJobIds =
                    savedJobsSnapshot.documents.map { it.getString("jobpost_id") }.filterNotNull()

//                Log.d("SavedJob", "Saved Job IDs: $savedJobIds")

                val jobApplicationsSnapshot = db.collection("JobApplication")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val jobApplicationMap = jobApplicationsSnapshot.documents.associate { doc ->
                    val jobpostId = doc.getString("jobpost_id") ?: ""
                    jobpostId to doc.getString("jobApplication_status")
                }


                // Map job posts and check if it's favorited
                jobPostsSnapshot.documents.forEach { doc ->
                    val job = doc.toObject(JobListingUiState::class.java)
                    job?.let {
                        it.isFavor = savedJobIds.contains(doc.id)
                        it.jobApplication_status = jobApplicationMap[doc.id]
                            ?: "Not Applied" // Default status if not applied

                        jobPosts.add(it)

                        Log.d("JobPost", "Job ID: ${job.jobPost_id}, isFavor: ${it.isFavor}")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors that occur
            }
//            finally {
//                _isLoading.value = false // Hide loading
//            }
        }

        return jobPosts
    }


    suspend fun getAppliedJobsForJobseeker(jobseekerId: String): List<Pair<JobApplicationUiState, JobListingUiState>> {
        val db = FirebaseFirestore.getInstance()
        val savedJobsWithDetails = mutableListOf<Pair<JobApplicationUiState, JobListingUiState>>()

        _isLoading.value = true

        withContext(Dispatchers.IO) {
            try {
                // Step 1: Query JobApplication collection for the given jobseeker_id
                val appliedJobsSnapshot = db.collection("JobApplication")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val savedJobsSnapshot = db.collection("SavedJob")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val savedJobIds =
                    savedJobsSnapshot.documents.map { it.getString("jobpost_id") }.filterNotNull()

                val appliedJobs = appliedJobsSnapshot.toObjects(JobApplicationUiState::class.java)

                // Step 2: For each saved job, retrieve the related job post details
                for (appliedJob in appliedJobs) {
                    val jobPostSnapshot = db.collection("Jobpost")
                        .document(appliedJob.jobpost_id)
                        .get()
                        .await()

                    if (jobPostSnapshot.exists()) {
                        val jobPost = jobPostSnapshot.toObject(JobListingUiState::class.java)
                        if (jobPost != null) {
                            jobPost.isFavor = savedJobIds.contains(appliedJob.jobpost_id)


                            // Step 3: Combine SavedJob and JobPost information
                            savedJobsWithDetails.add(Pair(appliedJob, jobPost))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors that occur
            } finally {
                _isLoading.value = false // Hide loading
            }
        }

        return savedJobsWithDetails
    }

    suspend fun getSavedJobsForJobseeker(jobseekerId: String): List<Pair<SavedJobUiState, JobListingUiState>> {
        val db = FirebaseFirestore.getInstance()
        val savedJobsWithDetails = mutableListOf<Pair<SavedJobUiState, JobListingUiState>>()

        _isLoading.value = true

        withContext(Dispatchers.IO) {
            try {
                // Step 1: Query SavedJob collection for the given jobseeker_id
                val savedJobsSnapshot = db.collection("SavedJob")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val savedJobs = savedJobsSnapshot.toObjects(SavedJobUiState::class.java)

                val jobApplicationsSnapshot = db.collection("JobApplication")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val jobApplicationMap = jobApplicationsSnapshot.documents.groupBy(
                    { it.getString("jobpost_id") ?: "" },  // JobPostId as key
                    {
                        JobApplicationUiState(
                            jobApplication_id = it.id,
                            jobApplication_status = it.getString("jobApplication_status")
                                ?: "Not Applied"
                        )
                    }
                )

                // Step 2: For each saved job, retrieve the related job post details
                for (savedJob in savedJobs) {
                    val jobPostSnapshot = db.collection("Jobpost")
                        .document(savedJob.jobpost_id)
                        .get()
                        .await()

                    if (jobPostSnapshot.exists()) {
                        val jobPost = jobPostSnapshot.toObject(JobListingUiState::class.java)
                        if (jobPost != null) {

                            val jobApplicationStatus =
                                jobApplicationMap[savedJob.jobpost_id]?.firstOrNull()?.jobApplication_status
                            jobPost.jobApplication_status =
                                jobApplicationStatus ?: "Not Applied" // Default if not applied


                            // Step 3: Combine SavedJob and JobPost information
                            savedJobsWithDetails.add(Pair(savedJob, jobPost))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle any errors that occur
            } finally {
                _isLoading.value = false // Hide loading
            }
        }

        return savedJobsWithDetails
    }


    suspend fun getNotificationForJobseeker(jobseekerId: String): List<JobseekerNotificationUiState> {
        val db = FirebaseFirestore.getInstance()
        val notificationList = mutableListOf<JobseekerNotificationUiState>()

        _isLoading.value = true

        withContext(Dispatchers.IO) {
            try {
                // Step 1: Query SavedJob collection for the given jobseeker_id
                val jobNotificationSnapshot = db.collection("JobseekerNotification")
                    .whereEqualTo("jobseeker_id", jobseekerId)
                    .get()
                    .await()

                val savedJobs =
                    jobNotificationSnapshot.toObjects(JobseekerNotificationUiState::class.java)

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


//    fun getJobseeker(jobseekerId: String) = CoroutineScope(Dispatchers.IO).launch {
//        val fireStoreRef = Firebase.firestore
//            .collection("Jobseeker")
//            .document(jobseekerId)
//
//
//        try {
//            fireStoreRef.get()
//                .addOnSuccessListener {
//                    // for getting single or particular document
//                    if (it.exists()) {
//                        val userData = it.toObject<UserUiState>()!!
//
//                        setProfile(userData)
//
//                        Log.d(TAG, "Data: ${it.data}")
//                    } else {
//                        Log.d(TAG, "No such document")
//                    }
//                }
//        } catch (e: Exception) {
//            Log.d(TAG, "Error getting jobseeker")
//        }
//    }


    fun retrieveData(
        userID: String,
        context: Context,
        data: (UserData) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(userID)

        try {
            fireStoreRef.get()
                .addOnSuccessListener {
                    // for getting single or particular document
                    if (it.exists()) {
                        val userData = it.toObject<UserData>()!!
                        data(userData)
                    } else {
                        Toast.makeText(context, "No User Data Found", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteData(
        userID: String,
        context: Context,
        navController: NavController,
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("user")
            .document(userID)

        try {
            fireStoreRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully deleted data", Toast.LENGTH_SHORT)
                        .show()
                    navController.popBackStack()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun setJobseekerFullName(fullname: String) {
        _uiState_Jobseeker.update { currentState ->
            currentState.copy(
                jobseeker_full_name = fullname
            )
        }
    }

    fun setJobseekerEmail(email: String) {
        _uiState_Jobseeker.update { currentState ->
            currentState.copy(
                jobseeker_email = email
            )
        }
    }

    fun setJobseekerPassword(password: String) {
        _uiState_Jobseeker.update { currentState ->
            currentState.copy(
                jobseeker_password = password
            )
        }
    }

    fun isMatched(): Boolean{
        return _uiState_Jobseeker.value.jobseeker_password == repeatPassword
    }

    fun isEmailRegistered(email: String, callback: (Boolean) -> Unit) {
        Firebase.firestore.collection("Jobseeker")
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

    @RequiresApi(Build.VERSION_CODES.P)
    fun saveData(
        jobseekerData: UserUiState,
        context: Context
    ) = CoroutineScope(Dispatchers.IO).launch {

        val fireStoreRef = Firebase.firestore
            .collection("Jobseeker")
            .document(jobseekerData.jobseeker_email)

        try {
            fireStoreRef.set(jobseekerData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully saved data", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun getJobseekerData(jobseekerId: String) = CoroutineScope(Dispatchers.IO).launch {
        val fireStoreRef = Firebase.firestore
            .collection("Jobseeker")
            .document(jobseekerId)

        try {
            // Use await() to turn the Firestore task into a suspend function
            val documentSnapshot = fireStoreRef.get().await()

            // Check if the document exists
            if (documentSnapshot.exists()) {
                // Extract specific fields from the document (email and password)
                getJobseekerEmail = documentSnapshot.getString("jobseeker_email").toString()
                getJobseekerPassword = documentSnapshot.getString("jobseeker_password").toString()

                if (getJobseekerEmail != "" && getJobseekerPassword != "") {
                    Log.d(TAG, "Email: $getJobseekerEmail, Password: $getJobseekerPassword")

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



    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun sendRecruiterNotification(jobPostId:String, jobPostTitle: String, recruiterId: String, jobseekerId: String) {
        val db = FirebaseFirestore.getInstance()

        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val notification = RecruiterNotificationUiState(
                recruiter_notification_title = jobPostTitle,
               recruiter_notification_date = currentDate,
                 recruiter_notification_brief_text = "Someone apply your job posting!",
             recruiter_notification_detail_text = "${jobseekerId} jobseeker has applied your job posting ${jobPostId}",
             recruiter_notification_sender = jobseekerId,
                recruiter_notification_isRead = false,
            recruiter_id = recruiterId,
            jobpost_id = jobPostId,
        )


        withContext(Dispatchers.IO) {
            try {
                // Use the add() method to save to the Firestore database
                val documentReference = db.collection("RecruiterNotification")
                    .add(notification)
                    .await() // Await the completion of the operation

                val generatedId = documentReference.id

                db.collection("RecruiterNotification")
                    .document(generatedId)
                    .update("recruiterNotification_id", generatedId) // Update the field
                    .await() // Await the update operation

                Log.d("RecruiterNotification", "Notification recruiter added with ID: ${documentReference.id}")
            } catch (e: Exception) {
                Log.e("RecruiterNotification", "Error adding Notification recruiter: ${e.message}", e)
                e.printStackTrace()
                // Handle any errors that occur
            }
        }
    }

    fun loginJobseeker(
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
        db.collection("Jobseeker").document(email).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val storedEmail = document.getString("jobseeker_email") ?: ""
                    val storedPassword = document.getString("jobseeker_password") ?: ""
                    getJobseekerEmail = document.getString("jobseeker_email") ?: ""
                    getJobseekerFullName = document.getString("jobseeker_full_name") ?: ""

                    val profile = UserUiState(
                        jobseeker_full_name = getJobseekerFullName,
                        jobseeker_email = getJobseekerEmail,
                        jobseeker_password = storedPassword
                    )

                    setProfile(profile)

                    // Validate email and password
                    if (email == storedEmail) {
                        if (password == storedPassword) {
                            // Successful login
                            Toast.makeText(context, "Login Successfully!", Toast.LENGTH_SHORT).show()
                            navController.navigate(Graph.MainScreenGraph) {
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





//    private val _uiState = MutableStateFlow(JobseekerNotificationUiState())
//    val uiState: StateFlow<JobseekerNotificationUiState> = _uiState.asStateFlow()
//
//    fun setJobseekerNotification(notification: JobseekerNotificationUiState) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                title = notification.title,
//                date = notification.date,
//                briefText = notification.briefText,
//                detailText = notification.detailText,
//                sender = notification.sender,
//                isRead = notification.isRead,
//
//            )
//        }
//    }
//
//    fun resetJobseekerNotification() {
//        _uiState.value = JobseekerNotificationUiState()
//    }