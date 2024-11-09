package com.work.workhubpro.ui.screens.projectform



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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.work.workhubpro.ui.theme.Lightblue2

@Composable
fun MyTextField(
    labelValue: String,
    painterResource: Painter,
    textValue: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        colors=OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.hsl(220f,0.8f,0.5f),
        ),

        value = textValue,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        ,
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = null, modifier = Modifier.size(24.dp))
        },
        label = {
            Text(text = labelValue, color = Color.DarkGray)
        },
        isError = isError
    )
}
data class SelectedOptions(
    val selectedEmployees: List<User> = emptyList(),
    val selectedProjectLeads: List<User> = emptyList()
)



@SuppressLint("StateFlowValueCalledInComposition", "SuspiciousIndentation")
@Composable
fun CreateProject(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    var name by remember { mutableStateOf("") }
    val projectFormViewModel: ProjectFormmViewMoel = hiltViewModel<ProjectFormmViewMoel>()
    var description by remember { mutableStateOf("") }
    var selectedOptions by remember { mutableStateOf(SelectedOptions()) }
    val scrollState = rememberScrollState()
    var workhub_Id = sharedViewModel.user.value?.id
    println(workhub_Id)
    LaunchedEffect(Unit) {
        if (workhub_Id != null) {
            projectFormViewModel.getProjectLeaders(workhub_Id)
        }
        if (workhub_Id != null) {
            projectFormViewModel.getemployees(workhub_Id)
        }
    }

    val projectleaders = projectFormViewModel.project_lead.collectAsState().value
    val employees = projectFormViewModel.employees.collectAsState().value

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Lightblue2)
            .padding(30.dp),

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
                .background(Lightblue2)
        ) {
            NormalTextComposable(value = stringResource(id = R.string.Create_New_project))
            HeadingTextComposable(value = stringResource(id = R.string.create_project))

            MyTextField(
                labelValue = stringResource(id = R.string.project_name),
                painterResource = painterResource(id = R.drawable.company_symbol),
                textValue = name,
                onValueChange = { name = it }
            )
            MyTextField(
                labelValue = stringResource(id = R.string.project_description),
                painterResource = painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = description,
                onValueChange = { description = it }
            )

            // Dropdown for selecting employees
            MultiSelectField(
                options = employees ?: emptyList(),
                selectedOptions = selectedOptions.selectedEmployees,
                onOptionsSelected = { options ->
                    selectedOptions = selectedOptions.copy(selectedEmployees = options)
                },
                label = "Select Employees"
            )

            // Dropdown for selecting project leaders
            MultiSelectField(
                options = projectleaders ?: emptyList(),
                selectedOptions = selectedOptions.selectedProjectLeads,
                onOptionsSelected = { options ->
                    selectedOptions = selectedOptions.copy(selectedProjectLeads = options)
                },
                label = "Select Project Leaders"
            )

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.hsl(220f,0.8f,0.5f)),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(
                        color = Color.hsl(248f, 0.95f, 0.60f), // Valid form color
                        shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (workhub_Id != null) {
                        projectFormViewModel.createProject(name,description,
                            selectedOptions.selectedProjectLeads[0].username,
                            selectedOptions.selectedEmployees.map { it },workhub_Id)
                    }
                }
            ) {
                Text(text = "Create Project", color = Color.White)
            }
        }
    }
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

    Column (
    ){
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Lightblue2),
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
                .background(Lightblue2)
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
            .padding(16.dp)
            .background(Lightblue2),
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