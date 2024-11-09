package com.work.workhubpro.ui.screens.projects

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.Project
import com.work.workhubpro.ui.composables.ProjectHeading
import com.work.workhubpro.ui.navigation.Navscreen

@Composable
fun ProjectListScreens(navController: NavController, sharedViewModel: SharedViewModel) {
    val projectsListViewModel: ProjectsListViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        projectsListViewModel.getallprojects(sharedViewModel.user.value?.id.toString())
    }

    val projects by projectsListViewModel.projects.collectAsState()

    val colors = listOf(Color(0xFF225F69), Color(0xE604241F)) // Blue and Purple gradient
    val rippleColor = Color.White.copy(alpha = 0.2f)
    val gradient = Brush.linearGradient(
        colors = colors, start = Offset.Zero, end = Offset.Infinite
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)) // Dark background color
    ) {
        ProjectHeading(value = stringResource(id = R.string.Projects_list))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(projects.distinct()) { project ->
                ProjectItemCard(project = project, navController = navController, gradient = gradient, rippleColor = rippleColor)
            }
        }
    }
}

@Composable
fun ProjectItemCard(project: Project, navController: NavController, gradient: Brush, rippleColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(MaterialTheme.shapes.medium)
            .animateContentSize()
            .background(gradient)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false, color = rippleColor),
                onClick = {
                    navController.navigate(Navscreen.ProjectDetails.route + "/${project.name}")
                }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.wall), // Placeholder image resource
            contentDescription = stringResource(id = R.string.Profile_photo),
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.medium)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = project.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = project.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.LightGray
            )
        }
    }
}
