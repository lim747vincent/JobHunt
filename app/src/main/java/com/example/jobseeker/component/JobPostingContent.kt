package com.example.jobseeker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobseeker.R

@Composable
fun JobPostingContent() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Image(painter = painterResource(id = R.drawable.softspace), contentDescription = "SoftSpace")
        // Job Title Section
        Text(
            text = "Software Engineer",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Company Information
        Text(
            text = "Soft Space SDN. BHD.",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Posted 1d ago",
            fontSize = 10.sp,
            fontWeight = FontWeight.Thin,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Job Info Row (Location, Category, Job Type)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_location_pin_24),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Kuala Lumpur", color = Color.White)
            }

            // Category
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_work_outline_24),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Developers / Programmers", color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ){
            // Job Type and Salary
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_work_24),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "Full Time", fontWeight = FontWeight.SemiBold, color = Color.White)
            }


            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(R.drawable.baseline_money),
                    contentDescription = "Location Icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Text(text = "RM 3,000 – RM 5,000 per month", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Job Description
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Join Our Innovative Fintech Team: Java Developer",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Ready to Simplify Payment with and make a real impact on the future of finance? We're looking for a passionate, innovative Java Developer to join our dynamic team. Whether you thrive as an independent problem-solver or a collaborative team leader with a strategic mindset, this role offers a chance to shape the next generation of our Merchant and Issuing solutions!",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "What You'll Do:\n" +
                            "- Dive into the heart of our Java codebase, crafting clean, secure, and maintainable code for our payment solutions.\n" +
                            "- Tackle complex technical challenges – your out-of-the-box thinking, resilience, and persistence will be essential!\n" +
                            "- Champion best practices in design, development, and security, ensuring our systems are rock solid and reflect the highest ethical standards.\u2028\u2028We're looking for someone who:\n" +
                            "- Required years of experience 2 years. Fresh graduates are encouraged to apply.\n" +
                            "- Understands the importance of secure coding practices (OWASP, PCI, etc.) and performance optimization.\n" +
                            "- Has a strong foundation in database design and SQL (MySQL, MariaDB, MSSQL, etc.)\n" +
                            "- Proficient with modern API design and integration technologies (RESTful, JSON, OpenAPI, etc.), with a solid understanding of both legacy protocols (SOAP) and modern standards.\n" +
                            "- Possesses strong familiarity with front-end technologies (HTML, CSS, JavaScript) and experience with at least one modern framework (React, Angular, Vue.js, or similar).\n" +
                            "- Experience with microservices architecture and distributed systems design.\n" +
                            "- Proven ability to deliver high-quality, scalable backend systems.\n" +
                            "- Can troubleshoot complex issues, break them down into smaller problems, and explain technical concepts with clarity and confidence.",
                    fontSize = 12.sp,
                    color = Color.Black
                )

            }
        }
    }
}

@Preview
@Composable
private fun JobPostingContentPreview(){
    JobPostingContent()
}