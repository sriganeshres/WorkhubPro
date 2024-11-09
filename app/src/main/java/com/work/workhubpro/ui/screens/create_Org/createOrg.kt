package com.work.workhubpro.ui.screens.create_Org

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.ui.composables.HeadingTextComposable
import com.work.workhubpro.ui.composables.NormalTextComposable
import com.work.workhubpro.ui.navigation.Navscreen
import com.work.workhubpro.ui.screens.CreateOrg.CreateOrganisationViewModel
import java.util.regex.Pattern

@Composable
fun CreateOrgScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    showPopup: MutableState<Boolean> = mutableStateOf(false),
    popupMessage: MutableState<String> = mutableStateOf("")
) {
    var organisationName by remember { mutableStateOf("") }
    var domainName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var adminEmail by remember { mutableStateOf("") }
    val role = "admin"

    val createOrgViewModel: CreateOrganisationViewModel = hiltViewModel()
    val idState = createOrgViewModel.id.collectAsState().value
    val adminData = createOrgViewModel.admin.collectAsState().value
    val token = createOrgViewModel.token.collectAsState().value
    val scrollState = rememberScrollState()
    val tokenManager = createOrgViewModel.getTokenManager()
    var isTermsAccepted by remember { mutableStateOf(false) }

    val isFormValid = remember {
        derivedStateOf {
            isEmailValid(adminEmail) && isPasswordValid(password) && isTermsAccepted
        }
    }
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00B0F0), // Start color
            Color(0xFF0077C2)  // End color
        )
    )


    if (showPopup.value) {
        AlertDialog(
            onDismissRequest = { showPopup.value = false },
            title = { Text(text = "Alert") },
            text = { Text(text = popupMessage.value) },
            confirmButton = {
                TextButton(onClick = { showPopup.value = false }) {
                    Text(text = "OK")
                }
            }
        )
    }
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            content = {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Alert") },
                    text = { Text(text = popupMessage.value) },
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text(text = "OK")
                        }
                    }
                )
            }
        )
    }

    @Composable
    fun showToast(message: String) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
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
            NormalTextComposable(value = stringResource(id = R.string.hey_there))
            HeadingTextComposable(value = stringResource(id = R.string.create_Org))

            MyTextField(
                labelValue = stringResource(id = R.string.organisation_name),
                painterResource(id = R.drawable.company_symbol),
                textValue = organisationName,
                onValueChange = { organisationName = it }
            )
            MyTextField(
                labelValue = stringResource(id = R.string.domain_name),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = domainName,
                onValueChange = { domainName = it }
            )
            MyTextField(
                labelValue = stringResource(id = R.string.description),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = description,
                onValueChange = { description = it }
            )


            Spacer(modifier = Modifier.height(30.dp))
            HeadingTextComposable(value = stringResource(id = R.string.admin_Details))
            Spacer(modifier = Modifier.height(30.dp))

            MyTextField(
                labelValue = stringResource(id = R.string.first_name),
                painterResource(id = R.drawable.admin_symbol),
                textValue = firstName,
                onValueChange = { firstName = it }
            )

            MyTextField(
                labelValue = stringResource(id = R.string.last_name),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = lastName,
                onValueChange = { lastName = it }
            )

            MyTextField(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.outline_mail_outline_black_20),
                textValue = adminEmail,
                onValueChange = { adminEmail = it },
                isError = !isEmailValid(adminEmail)
            )


            PasswordTextField(
                labelValue = stringResource(id = R.string.password),
                painterResource(id = R.drawable.outline_password_black_20),
                textValue = password,
                onValueChange = { password = it },
                isError = !isPasswordValid(password)
            )


            CheckBoxComposable(
                value = stringResource(id = R.string.terms_and_conditions),
                isChecked = isTermsAccepted,
                onCheckedChange = { isTermsAccepted = it }
            )

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(
                        color = if (isFormValid.value) {
                            Color.hsl(248f, 0.95f, 0.60f) // Valid form color
                        } else {
                            Color.hsl(210f,0.34f,0.60f)// Invalid form color
                        },
                    shape = RoundedCornerShape(10.dp)),
                shape = RoundedCornerShape(10.dp),
//                @Composable
                onClick = {
                    if (isFormValid.value) {
                        createOrgViewModel.createOrg(
                            organisationName,
                            description,
                            firstName + " " + lastName,
                            domainName
                        )
                    }
                },
                enabled = isFormValid.value
            ) {
                Text(text = "Create Org",color=Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
            LaunchedEffect(idState) {
                try {
                    println(idState)
                    println("Type of idState: ${idState.javaClass.name}")

                    if (idState != 0) { // Assuming 0 represents an initial state or default value
                        createOrgViewModel.signupUser(
                            firstName + " " + lastName,
                            adminEmail,
                            password,
                            idState,
                            role
                        )
                    }
                } catch (e: Exception) {
                    showPopup.value = true
                    popupMessage.value = "Error occurred: ${e.message}"
                }
            }
            LaunchedEffect(adminData) {
                try {
                    println("i am sung jo ")
                    println(adminData)
                    if (adminData != null) {
                        sharedViewModel.updateUser(adminData)
                        tokenManager.saveToken(token)
                        navController.navigate(Navscreen.Bottom.route + "/$firstName")
                    }
                } catch (e: Exception) {
                    showPopup.value = true
                    popupMessage.value = "Error occurred: ${e.message}"
                }
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
            .padding(10.dp)
        ,
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = null, modifier = Modifier.size(24.dp))
        },
        label = {
            Text(text = labelValue)
        },
        isError = isError
    )
}

@Composable
fun PasswordTextField(
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
            .fillMaxWidth()
            .padding(10.dp)
        ,
        leadingIcon = {
            Icon(painter = painterResource, contentDescription = null, modifier = Modifier.size(24.dp))
        },
        label = {
            Text(text = labelValue)
        },
        visualTransformation = PasswordVisualTransformation(),
        isError = isError
    )
}

@Composable
fun CheckBoxComposable(
    value: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
        Text(text = value)
    }
}

fun isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
//
fun isPasswordValid(password: String): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$"
    val pattern = Pattern.compile(passwordPattern)
    val matcher = pattern.matcher(password)
    return matcher.matches()
}


