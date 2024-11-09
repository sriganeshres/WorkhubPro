package com.work.workhubpro.ui.screens.loginPage

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
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.ui.composables.HeadingTextComposable
import com.work.workhubpro.ui.composables.NormalTextComposable
import com.work.workhubpro.ui.navigation.Navscreen
import kotlinx.coroutines.launch
import java.util.regex.Pattern
@Composable
fun LoginScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val loginViewModel: LoginPageViewModel = hiltViewModel<LoginPageViewModel>()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val role = "admin"
    val user=loginViewModel.user.collectAsState().value
    val token=loginViewModel.token.collectAsState().value
    val tokenManager=loginViewModel.getTokenManager()

    val scrollState = rememberScrollState()
    var isTermsAccepted by remember { mutableStateOf(false) }
    var loginSuccess by remember { mutableStateOf(false) }
    var showInvalidCredentialsPopup by remember { mutableStateOf(false) }

    val isFormValid = remember {
        derivedStateOf {
            isEmailValid(email) && isPasswordValid(password) && isTermsAccepted
        }
    }
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00B0F0), // Start color
            Color(0xFF0077C2)  // End color
        )
    )

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
            HeadingTextComposable(value = stringResource(id = R.string.login))

            Spacer(modifier = Modifier.height(30.dp))
            HeadingTextComposable(value = stringResource(id = R.string.Login))
            Spacer(modifier = Modifier.height(30.dp))

            MyTextField(
                labelValue = stringResource(id = R.string.first_name),
                painterResource(id = R.drawable.admin_symbol),
                textValue = username,
                onValueChange = { username = it }
            )

            MyTextField(
                labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.outline_mail_outline_black_20),
                textValue = email,
                onValueChange = { email = it },
                isError = !isEmailValid(email)
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

            val coroutineScope = rememberCoroutineScope()

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .background(
                        color = if (isFormValid.value) {
                            Color.hsl(248f, 0.95f, 0.60f) // Valid form color
                        } else {
                            Color.hsl(210f, 0.34f, 0.60f)// Invalid form color
                        },
                        shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (isFormValid.value) {
                        coroutineScope.launch {
                            val success = loginViewModel.loginuser(username, email, password)

                        }
                    }
                }
            )
            {
                Text(text = "Login",color=Color.White)
            }
            LaunchedEffect(user){
                if (user!=null) {
                    loginSuccess = true
                    println("hi batoske")
                    println(token)
                    println(tokenManager.getToken())
                    tokenManager.saveToken(token)
                    sharedViewModel.updateUser(user)
                    navController.navigate(Navscreen.Bottom.route + "/${username}")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Show invalid credentials popup if needed
    if (showInvalidCredentialsPopup) {
        AlertDialog(
            onDismissRequest = { showInvalidCredentialsPopup = false },
            title = { Text(text = "Invalid Credentials") },
            text = { Text(text = "Please check your username and password and try again.") },
            confirmButton = {
                TextButton(onClick = { showInvalidCredentialsPopup = false }) {
                    Text(text = "OK")
                }
            }
        )
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
