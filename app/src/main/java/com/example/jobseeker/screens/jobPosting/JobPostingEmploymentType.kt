package com.example.jobseeker.screens.jobPosting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jobseeker.R
import com.example.jobseeker.viewModel.RecruiterViewModel
import kotlinx.coroutines.selects.select

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobPostingEmploymentType(
    rootNavController: NavController,
    onNextButtonClicked : () -> Unit,
    recruiterViewModel : RecruiterViewModel
) {

    val jobUiState by recruiterViewModel.uiState_JobPost.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(id = R.color.top_bar_color)),
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                    ) {
                        Text(
                            text = "Job Posting Module",
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
                .background(colorResource(id = R.color.background_purple))
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Employment Type",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = colorResource(id = R.color.light_purple),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Divider(color = Color.White, modifier = Modifier.padding(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color(0xFF5B5D92), shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                EmploymentTypeRadioGroup(selectedOption = jobUiState.jobpost_employment_type) { selected ->
                    recruiterViewModel.setJobPostCompanyEmploymentType(selected)
                }
            }

            //Spacer(modifier = Modifier.height(8.dp))

            // Language Skills Section
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .background(Color(0xFF5B5D92), shape = RoundedCornerShape(8.dp))
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = "Any required language skills? (optional)",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    ),
//                    color = Color.White
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                LanguageSkillRow(language, languageLevel) { lang, level ->
//                    language = lang
//                    languageLevel = level
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Experience Section
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//                    .background(Color(0xFF5B5D92), shape = RoundedCornerShape(8.dp))
//                    .padding(16.dp)
//            ) {
//                Text(
//                    text = "Does this role require experience?",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold
//                    ),
//                    color = Color.White
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//                ExperienceRadioGroup(selectedOption = requiresExperience) { selected ->
//                    requiresExperience = selected
//                }
//            }

            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    if(jobUiState.jobpost_employment_type == "Part-time" || jobUiState.jobpost_employment_type == "Full-time"){
                        onNextButtonClicked()
                    }else{
                        Toast.makeText(context, "Please choose an employment type.", Toast.LENGTH_SHORT).show()
                    }
                     },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C93FF))
            ) {
                Text(text = "Next", color = colorResource(id = R.color.light_purple))
            }
        }
    }
}

@Composable
fun EmploymentTypeRadioGroup(selectedOption: String, onOptionSelected: (String) -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == "Part-time",
                onClick = { onOptionSelected("Part-time") },
                colors = RadioButtonDefaults.colors(colorResource(id = R.color.light_purple))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Part-time", color = colorResource(id = R.color.light_purple))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = selectedOption == "Full-time",
                onClick = { onOptionSelected("Full-time") },
                colors = RadioButtonDefaults.colors(colorResource(id = R.color.light_purple))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Full-time", color = colorResource(id = R.color.light_purple))
        }
    }
}

//@Composable
//fun LanguageSkillRow(
//    selectedLanguage: String,
//    selectedLevel: String,
//    onValueChanged: (String, String) -> Unit
//) {
//    var expandedLanguage by remember { mutableStateOf(false) }
//    var expandedLevel by remember { mutableStateOf(false) }
//
//    val languages = listOf("English", "Malay", "Chinese")
//    val levels = listOf("Beginner", "Intermediate", "Proficient")
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        // Language dropdown
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .padding(end = 8.dp)
//        ) {
//            OutlinedTextField(
//                value = selectedLanguage,
//                onValueChange = { },
//                label = { Text("Language", color = Color.Gray) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { expandedLanguage = true },
//                trailingIcon = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
//                        contentDescription = null
//                    )
//                },
//                readOnly = true
//            )
//
//            DropdownMenu(
//                expanded = expandedLanguage,
//                onDismissRequest = { expandedLanguage = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                languages.forEach { language ->
//                    DropdownMenuItem(
//                        text = { Text(text = language) },
//                        onClick = {
//                            onValueChanged(language, selectedLevel) // Update language
//                            expandedLanguage = false
//                        }
//                    )
//                }
//            }
//        }
//
//        // Level dropdown
//        Box(
//            modifier = Modifier
//                .weight(1f)
//                .padding(start = 8.dp)
//        ) {
//            OutlinedTextField(
//                value = selectedLevel,
//                onValueChange = { },
//                label = { Text("Level", color = Color.Gray) },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clickable { expandedLevel = true },
//                trailingIcon = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
//                        contentDescription = null
//                    )
//                },
//                readOnly = true
//            )
//
//            DropdownMenu(
//                expanded = expandedLevel,
//                onDismissRequest = { expandedLevel = false },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                levels.forEach { level ->
//                    DropdownMenuItem(
//                        text = { Text(text = level) },
//                        onClick = {
//                            onValueChanged(selectedLanguage, level)
//                            expandedLevel = false
//                        }
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun ExperienceRadioGroup(selectedOption: String, onOptionSelected: (String) -> Unit) {
//    Column {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(
//                selected = selectedOption == "Yes",
//                onClick = { onOptionSelected("Yes") },
//                colors = RadioButtonDefaults.colors(Color.White)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "Yes", color = Color.White)
//        }
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            RadioButton(
//                selected = selectedOption == "No",
//                onClick = { onOptionSelected("No") },
//                colors = RadioButtonDefaults.colors(Color.White)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Text(text = "No", color = Color.White)
//        }
//    }
//}

@Preview
@Composable
private fun PreviewJobPostingEmploymentType() {
    JobPostingEmploymentType(rootNavController = rememberNavController(), onNextButtonClicked = {}, recruiterViewModel = viewModel())
}