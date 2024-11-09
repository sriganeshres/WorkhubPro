package com.work.workhubpro.ui.screens.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.ui.composables.CheckBoxComposable
import com.work.workhubpro.ui.composables.HeadingTextComposable
import com.work.workhubpro.ui.composables.MyTextField
import com.work.workhubpro.ui.composables.NormalTextComposable
import com.work.workhubpro.ui.composables.PasswordTextField
import com.work.workhubpro.ui.navigation.Navscreen

@Composable
fun SignupScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val signupViewModel: SignupViewModel = hiltViewModel<SignupViewModel>()
    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NormalTextComposable(value = stringResource(id = R.string.hey_there))
            HeadingTextComposable(value = stringResource(id = R.string.create_account))
            Spacer(modifier = Modifier.height(20.dp))
            MyTextField(
                labelValue = stringResource(id = R.string.first_name),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = firstName,
                onValueChange = { firstName = it }

            )
            MyTextField(labelValue = stringResource(id = R.string.last_name),
                painterResource(id = R.drawable.outline_edit_black_24dp),
                textValue = lastName,
                onValueChange = { lastName = it })
            MyTextField(labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.outline_mail_outline_black_20),
                textValue = email,
                onValueChange = { email = it })
            PasswordTextField(labelValue = stringResource(id = R.string.password),
                painterResource(id = R.drawable.outline_password_black_20),
                textValue = password,
                onValueChange = { password = it })
            MyTextField(labelValue = stringResource(id = R.string.email),
                painterResource(id = R.drawable.outline_mail_outline_black_20),
                textValue = email,
                onValueChange = { email = it })
            Spacer(modifier = Modifier.height(16.dp))
            CheckBoxComposable(value = stringResource(id = R.string.terms_and_conditions))
            Button(onClick = {
                signupViewModel.signupUser(firstName, email, password)
                navController.navigate(Navscreen.Bottom.route + "/${firstName}")
            }
            ) {
                Text(
                    text = "Signup",
//                    OnTextLayout={
//
//                    }
                )
            }
        }
    }
}
