package com.example.jobseeker.navigations

object Graph {
    const val RootGraph = "rootGraph"
    const val AuthGraph = "authGraph"
    const val MainScreenGraph = "mainScreenGraph"
    const val NotificationGraph = "notificationGraph"
    const val JobDetailGraph = "jobDetailGraph"
    const val RecruiterMainScreenGraph = "recruiterMainScreenGraph"
    const val CreditScoreModule = "creditScoreModuleGraph"
    const val JobPostingModule = "jobPostingModuleGraph"
    const val ApplicationModule = "applicationModuleGraph"
    const val TopUpGraph = "topUpGraph"
    const val ViewHistoryGraph = "viewHistoryGraph"
    const val SuccessfulGraph = "successfulGraph"
    const val MyCompanyGraph = "myCompanyGraph"
    const val ViewMyCompanyGraph = "viewMyCompanyGraph"
    const val AddNewCompanyGraph = "addNewCompanyGraph"
    const val AddNewJobGraph = "addNewJobGraph"
    const val JobPostingEmploymentGraph = "jobPostingEmploymentGraph"
    const val JobPostingSalaryGraph = "jobPostingSalaryGraph"
    const val JobPostingPhotoGraph = "jobPostingPhotoGraph"
    const val JobPostingFinalGraph = "jobPostingFinalGraph"
    const val JobPostingHistoryGraph = "jobPostingHistoryGraph"
    const val JobPostingSuccessfulGraph = "jobPostingSuccessfulGraph"
    const val ApplicantDetailsGraph = "applicantDetailsGraph"
    const val RecruiterNotificationGraph = "recruiterNotificationGraph"
}

sealed class AuthRouteScreen(val route: String) {
    object StartAuth : AuthRouteScreen("startAuth")
    object RecruiterLogin : AuthRouteScreen("recruiterLogin")
    object JobseekerLogin : AuthRouteScreen("jobseekerLogin")
    object RecruiterSignUp : AuthRouteScreen("recruiterSignUp")
    object JobseekerSignUp : AuthRouteScreen("jobseekerSignUp")
    object Forget : AuthRouteScreen("forget")
    object RecruiterForget : AuthRouteScreen("recruiterForget")
}

sealed class MainRouteScreen(var route: String) {

    object Home : MainRouteScreen("home")
    object Activity : MainRouteScreen("activity")
    object Notification : MainRouteScreen("notification")
    object Profile : MainRouteScreen("profile")

    object recruiterHome : MainRouteScreen("recruiterHome")
    object recruiterNotification : MainRouteScreen("recruiterNotification")
    object recruiterProfile : MainRouteScreen("recruiterProfile")
    object creditScoreModule : MainRouteScreen("creditScoreModule")
    object jobPostingModule : MainRouteScreen("jobPostingModule")
    object applicationModule : MainRouteScreen("applicationModule")
}

sealed class NotificationRouteScreen(var route: String) {
    object NotificationDetail : NotificationRouteScreen("notificationDetail?id={id}"){
        fun passNotificationId(
            id: String = "0"
        ): String {
            return "notificationDetail?id=$id"
        }
    }
}

sealed class RecruiterNotificationRouteScreen(var route: String) {
    object RecruiterNotificationDetail : RecruiterNotificationRouteScreen("recruiterNotificationDetail?id={id}"){
        fun passNotificationId(
            id: String = "0"
        ): String {
            return "recruiterNotificationDetail?id=$id"
        }
    }
}

sealed class JobDetailRouteScreen(var route: String){
    object JobDetail: JobDetailRouteScreen("jobDetail?id={id}"){
        fun passJobId(
            id: String = "0"
        ): String{
            return "jobDetail?id=$id"
        }
    }
    object ApplySuccess : JobDetailRouteScreen("applySuccess")
    object JobFiltering: JobDetailRouteScreen("jobFiltering")
    object FilteringResult: JobDetailRouteScreen("filteringResult")
}



//    object Detail : Screens(route = "detail_screen?id={id}&name={name}") {
//        fun passNameAndId(
//            id: Int = 0,
//            name: String = "Stevdza-San"
//        ): String {
//            return "detail_screen?id=$id&name=$name"
//        }
//    }
