package com.work.workhubpro.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.Task
import com.work.workhubpro.models.UpdateTask
import com.work.workhubpro.ui.navigation.Navscreen
import com.work.workhubpro.ui.theme.mediumblue
import java.time.LocalDate

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Home(name: String, navController: NavController, sharedViewModel: SharedViewModel) {
    val datedialogueState = rememberMaterialDialogState()
    val viewmodel: HomeViewModel = hiltViewModel()
    val workhub = viewmodel.workhub.collectAsState().value
    var name = "technovia"
    var description = "description"
    val homeViewModel: HomeViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        homeViewModel.gettasksToUser(sharedViewModel.user.value?.id!!)
    }
    val Tasks = homeViewModel.tasks.collectAsState().value
    println(workhub)
    if (workhub != null) {
        sharedViewModel.updateWorkhub(workhub)
        name = workhub.name
        description = workhub.description
    }

    LaunchedEffect(Unit) {
        viewmodel.getworkhub(sharedViewModel.user.value?.id.toString())
    }
    val font = FontFamily(Font(R.font.kaushanscript))
    val joseph = FontFamily(Font(R.font.josefinsansbold))
    val role = sharedViewModel.user.value?.role

    var showDialog by remember { mutableStateOf(false) }

    // Show the dialog when the composable is first composed
    LaunchedEffect(Unit) {
        showDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003366))))
    ) {
        Image(
            painter = painterResource(id = R.drawable.border),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
        )

        Text(
            text = name,
            style = TextStyle(
                fontFamily = font,
                fontSize = 35.sp,
                color = Color.White
            ),
            modifier = Modifier.padding(16.dp)
        )

        Surface(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .shadow(20.dp)
                .height(200.dp),
            color = mediumblue,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = joseph,
                    fontSize = 18.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(18.dp),

                )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Add your Text composable here
            Text(
                text = "Company Calendar",
                style = TextStyle(
                    fontFamily = font,
                    fontSize = 30.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(10.dp)
            )

            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = "Your Image Description",
                modifier = Modifier
                    .size(70.dp)
                    .shadow(10.dp)
                    .padding(10.dp)
                    .clickable { datedialogueState.show() },
            )

        }
        Spacer(modifier = Modifier.height(20.dp))

        // Render buttons based on user role
        when (role) {
            "admin" -> {
                AdminButtons(navController)
            }
            "ProjectLeader" -> {
                ProjectLeaderButtons(navController)
                ScrollableAssignedTasksList(Tasks)
            }
            "employee" -> {
                ScrollableAssignedTasksList(Tasks)
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = painterResource(id = R.drawable.admin_symbol),
                contentDescription = "Open Calendar",
                modifier = Modifier
                    .size(1.dp)
                    .padding(16.dp)
                    .clickable {
                        showDialog = true
                    }
            )
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {

            MaterialDialog(
                dialogState = datedialogueState,
                buttons = {
                    LaunchedEffect(Unit) {
                        showDialog = true
                    }
                }
            ) {
                datepicker(
                    initialDate = LocalDate.now()
                )
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    val navController = rememberNavController()
    Home(
        name = "Preview",
        navController = navController,
        sharedViewModel = SharedViewModel()
    )
}

@Composable
fun AdminButtons(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        AddTaskButton(navController)
        AddProjectButton(navController)
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        AddEmployeesButton(navController)
    }
}

@Composable
fun ProjectLeaderButtons(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp)
    ) {
        AddTaskButton(navController)
    }
}

@Composable
fun AddTaskButton(navController: NavController) {
    Surface(
        modifier = Modifier
            .shadow(20.dp)
            .fillMaxWidth(0.35f)
            .background(color = Color.Transparent),
        shape = RoundedCornerShape(8.dp)
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(220f, 0.8f, 0.5f)),

            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.hsl(265f, 0.55f, 0.50f),
                    shape = RoundedCornerShape(10.dp)
                ),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                navController.navigate(Navscreen.Createtask.route)
            },
        ) {
            Text(text = "Add Task", color = Color.White)
        }
    }
}

@Composable
fun AddProjectButton(navController: NavController) {
    Surface(
        modifier = Modifier
            .shadow(20.dp)
            .fillMaxWidth(0.5f)
            .background(color = Color.Transparent),
        shape = RoundedCornerShape(8.dp),

        ) {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(220f, 0.8f, 0.5f)),

            modifier = Modifier
                .background(
                    color = Color.hsl(265f, 0.55f, 0.50f),
                    shape = RoundedCornerShape(10.dp)
                ),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                navController.navigate(Navscreen.CreateProject.route)
            },
        ) {
            Text(text = "Add Project", color = Color.White)
        }
    }
}

@Composable
fun AddEmployeesButton(navController: NavController) {

    Surface(
        modifier = Modifier

            .padding(16.dp)
            .shadow(20.dp)
            .fillMaxWidth(0.7f)
            .background(color = Color.Transparent),
        shape = RoundedCornerShape(8.dp)
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(220f, 0.8f, 0.5f)),
            modifier = Modifier
                .background(
                    color = Color.Cyan
                )
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            onClick = {
                 navController.navigate(Navscreen.Addempoly.route)
            },
        ) {
            Text(
                text = "Add Employers",
                style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.josefinsansbold)),
                    fontSize = 16.sp,
                    color = Color.White
                )
            )
        }
    }
}
@Composable
fun ScrollableAssignedTasksList(tasks: List<Task>) {
    val taskList = remember { mutableStateListOf(*tasks.toTypedArray()) }
    val snackbarHostState = remember { SnackbarHostState() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        items(taskList) { task ->
            TaskItem(
                task = task,
            )
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                modifier = Modifier.padding(16.dp)
            )
        }
    )
}

@Composable
fun TaskItem(
    task: Task,
) {
    var isChecked by remember { mutableStateOf(task.status == "CompletedByUser") }
    val viewModel: HomeViewModel = hiltViewModel()
    val updateTask=UpdateTask(task.ID,task.status)
    val toggleCheckbox: () -> Unit = {
        isChecked = !isChecked
        // Add your logic here to mark the task as completed
        println(updateTask.taskID)
        if(isChecked){
            updateTask.status="CompletedByUser"
        }
        else{
            updateTask.status="NotCompletedByUser"
        }
        viewModel.updateTask(updateTask)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(shape = RoundedCornerShape(12.dp), width = 2.dp, color = Color.Cyan)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
            IconButton(
                onClick = { toggleCheckbox() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (isChecked) R.drawable.checked
                        else R.drawable.unchecked
                    ),
                    contentDescription = null,
                    tint = if (isChecked) Color.Green else Color.Gray
                )
            }
        }
    }
}
