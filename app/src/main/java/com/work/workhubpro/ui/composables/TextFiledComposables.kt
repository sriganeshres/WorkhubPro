package com.work.workhubpro.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MyTextField(
    labelValue: String,
    painterResource: Painter,
    textValue: String,
    onValueChange: (String) -> Unit
) {
    // Define the animation states
    val colorAnimation = animateColorAsState(if (textValue.isNotEmpty()) Color.Red else Color.Green)
    val sizeAnimation = animateDpAsState(targetValue = if (textValue.isNotEmpty()) 30.dp else 20.dp)

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue,
        onValueChange = onValueChange,
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions.Default,
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.hsl(
                hue = 199f,
                saturation = 0.9f,
                lightness = 0.73f
            ),
            focusedLabelColor = Color.DarkGray,
            cursorColor = Color.Black,
            unfocusedContainerColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "Mail",
                modifier = Modifier
                    .size(sizeAnimation.value, sizeAnimation.value), // Use animated size
            )
        }
    )
}


@Composable
fun PasswordTextField(
    labelValue: String,
    painterResource: Painter,
    textValue: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible: Boolean by remember { mutableStateOf(false) }
    val sizeAnimation = animateDpAsState(targetValue = if (textValue.isNotEmpty()) 30.dp else 20.dp)
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = textValue,
        onValueChange = onValueChange,
        label = { Text(text = labelValue) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        textStyle = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal,
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.hsl(hue = 199f, saturation = 0.9f, lightness = 0.73f),
            focusedLabelColor = Color.Blue.copy(alpha = 0.5f),
            cursorColor = Color.White,
            unfocusedContainerColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                painter = painterResource,
                contentDescription = "Mail",
                modifier = Modifier.size(sizeAnimation.value, sizeAnimation.value),
            )
        },
        trailingIcon = {
            val iconImage =
                if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description = if (passwordVisible) "Hide Password" else "Show Password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}
