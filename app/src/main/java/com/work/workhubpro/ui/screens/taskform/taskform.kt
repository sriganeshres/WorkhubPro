package com.work.workhubpro.ui.screens.taskform


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.User
import com.work.workhubpro.ui.composables.HeadingTextComposable
import com.work.workhubpro.ui.composables.NormalTextComposable

data class SelectedOptions(
    val selectedEmployees: List<User> = emptyList(),
    val selectedProjectLeads: List<User> = emptyList()
)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Create_task(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val assigned_by = sharedViewModel.user.collectAsState().value?.ID
    val work_hub_id = sharedViewModel.user.collectAsState().value?.id
    val project_key = sharedViewModel.user.collectAsState().value?.id
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var assignedTo by remember { mutableStateOf("") }
    
    val createTaskViewModel: TaskFormViewModel = hiltViewModel()
    val scrollState = rememberScrollState()
    var selectedOptions by remember { mutableStateOf(SelectedOptions()) }
    var workhub_Id = sharedViewModel.user.value?.id
    LaunchedEffect(Unit) {
        if (workhub_Id != null) {
            createTaskViewModel.getProjectLeaders(workhub_Id)
        }
        if (workhub_Id != null) {
            createTaskViewModel.getemployees(workhub_Id)
        }
    }

    val projectleaders = createTaskViewModel.project_lead.collectAsState().value
    val employees = createTaskViewModel.employees.collectAsState().value


    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00B0F0), // Start color
            Color(0xFF0077C2)  // End color
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(30.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState),

            ) {
            NormalTextComposable(value = stringResource(id = R.string.Create_New_Task))
//            HeadingTextComposable(value = stringResource(id = R.string.))

            MyTextField(
                labelValue = stringResource(id = R.string.task_name),
                painterResource(id = R.drawable.company_symbol),
                textValue = name,
                onValueChange = { name = it }
            )
            MyTextField(
                labelValue = stringResource(id = R.string.task_description),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = description,
                onValueChange = { description = it }
            )


            // Dropdown for selecting employees
            MultiSelectField(
                options = employees ?: emptyList(),
                selectedOptions = selectedOptions.selectedEmployees,
                onOptionsSelected = { 
                     options ->
                    selectedOptions = selectedOptions.copy(selectedEmployees = options)
                    
                },
                label = "Select Employees"
            )

            if (sharedViewModel.user.value?.role == "admin") {
                MultiSelectField(
                    options = projectleaders ?: emptyList(),
                    selectedOptions = selectedOptions.selectedProjectLeads,
                    onOptionsSelected = {
                         selectedUsers->val selectedIds = selectedUsers.map { it.id }
                        assignedTo = selectedIds.firstOrNull().toString()
                        selectedOptions = selectedOptions.copy(selectedProjectLeads = selectedUsers)
                        
                    },
                    label = "Select Project Leaders"
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(
                        color =
                        Color.hsl(248f, 0.95f, 0.60f), // Valid form color

                        shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    createTaskViewModel.createTask(name, description, assignedTo.toInt(),assigned_by!!,work_hub_id!!,project_key!!)
                },
            ) {
                Text(text = "Create Task", color = Color.White)
            }
        }
    }
}

@Composable
fun MyTextField(
    labelValue: String,
    painterResource: Painter,
    textValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = textValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        },
        label = {
            Text(text = labelValue)
        },
        isError = isError
    )
}


@Composable
fun MultiSelectField(
    options: List<User>,
    selectedOptions: List<User>,
    onOptionsSelected: (List<User>) -> Unit,
    label: String
) {
    var isExpanded by remember { mutableStateOf(false) }
    val icon = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown

    Column {
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { isExpanded = !isExpanded },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (selectedOptions.isNotEmpty()) {
                        selectedOptions.joinToString { it.username }
                    } else {
                        label
                    }
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }

        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            options.forEach { option ->
                CustomMenuItem(
                    user = option,
                    isSelected = selectedOptions.contains(option),
                    onSelect = { selectedUser ->
                        onOptionsSelected(
                            if (selectedOptions.contains(selectedUser)) {
                                selectedOptions.filterNot { it == selectedUser }
                            } else {
                                selectedOptions + selectedUser
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun CustomMenuItem(
    user: User,
    isSelected: Boolean,
    onSelect: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelect(user) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (isSelected) {
            Icon(
                painter = painterResource(id = R.drawable.outline_edit_black_24dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}



