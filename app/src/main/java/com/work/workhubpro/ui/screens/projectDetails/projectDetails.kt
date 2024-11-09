package com.work.workhubpro.ui.screens.projectDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.Project
import com.work.workhubpro.models.User

@Composable
fun ProjectDetails(id: String, navController: NavController, sharedViewModel: SharedViewModel) {
    val projectDetailsViewModel: ProjectDetailsViewModel = hiltViewModel()
    println(id)
    LaunchedEffect(Unit) {
        projectDetailsViewModel.getProjectDetails(id.toInt())
    }

    val project = projectDetailsViewModel.project.collectAsState().value
    var members : List<User> = emptyList()
    if(project!=null){
        members = project.Members!!
    }

    if (project != null) { // Check if project is not null
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = project.name,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = project.description,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Project Leader: ${project.projectLead}",
                modifier = Modifier.padding(bottom = 16.dp)
            )
//            Text(
//                text = "Members:",
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//            LazyColumn(
//                modifier = Modifier.fillMaxWidth(),
//                contentPadding = PaddingValues(8.dp)
//            ) {
//                items(members) { member ->
//                    MemberItem(member = member)
//                    Spacer(modifier = Modifier.height(8.dp))
//                }
//            }
        }
    } else {
        // Show a loading indicator or placeholder until project data is available
        Text(text = "Loading project details...")
    }
}

@Composable
fun MemberItem(member: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Assuming User has a field named 'name' and 'profileImage' to display
        Image(
            painter = painterResource(id = R.drawable.wall), // Replace with member's profileImage
            contentDescription = "Member Profile", modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = member.username,
        )
    }
}
