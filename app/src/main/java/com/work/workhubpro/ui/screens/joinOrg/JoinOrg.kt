package com.work.workhubpro.ui.screens.joinOrg

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.ui.composables.CheckBoxComposable
import com.work.workhubpro.ui.composables.HeadingTextComposable
import com.work.workhubpro.ui.composables.MyTextField
import com.work.workhubpro.ui.composables.NormalTextComposable
import com.work.workhubpro.ui.composables.PasswordTextField
import com.work.workhubpro.ui.navigation.Navscreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@Composable
fun JoinOrganization_Screen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    showPopup: MutableState<Boolean> = mutableStateOf(false),
    popupMessage: MutableState<String> = mutableStateOf("")
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var key by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    val joinOrgViewModel: JoinOrgViewModel = hiltViewModel()
    val id = joinOrgViewModel.id.collectAsState().value
    val tokenManager = joinOrgViewModel.getTokenManger()
    val token = joinOrgViewModel.token.collectAsState().value
    val user = joinOrgViewModel.user.collectAsState().value
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .verticalScroll(scrollState)
        ) {
            NormalTextComposable(value = stringResource(id = R.string.hey_there))
            HeadingTextComposable(value = stringResource(id = R.string.join_org))
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField(
                labelValue = stringResource(id = R.string.first_name),
                painterResource(id = R.drawable.outline_edit_black_24dp),
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
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = email,
                onValueChange = { email = it }
            )
            MyTextField(
                labelValue = stringResource(id = R.string.role),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = role,
                onValueChange = { role = it }
            )
            PasswordTextField(labelValue = stringResource(id = R.string.password),
                painterResource(id = R.drawable.outline_password_black_20),
                textValue = password,
                onValueChange = { password = it }
            )
            MyTextField(
                labelValue = stringResource(id = R.string.privacy_key),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = key,
                onValueChange = { key = it }
            )
            CheckBoxComposable(value = stringResource(id = R.string.terms_and_conditions))
            Button(
                onClick = {
                    runBlocking { joinOrgViewModel.getOrg(key.toInt()) }
                }
            ) {
                Text(text = "Join Org")
            }
            Spacer(modifier = Modifier.height(16.dp))
            LaunchedEffect(id) {
                try {
                    println("Checked")
                    println("id: $id")
                    if (id != 0) {
                        joinOrgViewModel.signupUser(
                            firstName + " " + lastName,
                            email,
                            password,
                            id = id,
                            role
                        )

                    }
                } catch (e: Exception) {
                    // Handle the exception gracefully
                    showPopup.value = true
                    popupMessage.value = "Error occurred: ${e.message}"
                }
            }
            LaunchedEffect(user) {
                if (user!=null) {
                    println(tokenManager.getToken())
                    tokenManager.saveToken(token)
                    sharedViewModel.updateUser(user)
                    navController.navigate(Navscreen.Bottom.route + "/$firstName")
                }
            }
            if (showPopup.value) {
                AlertDialog(
                    onDismissRequest = { showPopup.value = false },
                    title = { Text(text = "Error") },
                    text = { Text(text = popupMessage.value) },
                    confirmButton = {
                        Button(onClick = { showPopup.value = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}