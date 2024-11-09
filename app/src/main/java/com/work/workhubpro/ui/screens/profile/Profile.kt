package com.work.workhubpro.ui.screens.profile
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.User
import com.work.workhubpro.ui.navigation.Navscreen
import com.work.workhubpro.ui.theme.mediumblue2
import com.work.workhubpro.ui.theme.performace
import com.work.workhubpro.ui.theme.team

@Composable
fun Info(user: User) {
    val heading = FontFamily(Font(R.font.dmserif))
    val infont = FontFamily(Font(R.font.deliusswash))

    Column(
        modifier = Modifier
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        InfoSection("Email:", user.email, heading, infont)
        Spacer(modifier =  Modifier.height(16.dp))
        InfoSection("Attendance:", "13/365", heading, infont)
    }
}

@Composable
fun InfoSection(title: String, value: String, heading: FontFamily, infont: FontFamily) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        color = mediumblue2
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = heading,
                    fontSize = 26.sp,
                    color = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = TextStyle(
                    fontFamily = infont,
                    fontSize = 20.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Profile(navController: NavController, sharedViewModel: SharedViewModel) {
    val user = sharedViewModel.user.collectAsState().value
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val tokenManager = profileViewModel.getTokenManager()
    val heading = FontFamily(Font(R.font.dmserif))
    val dancing = FontFamily(Font(R.font.dancingscript))
    var showDialog by remember { mutableStateOf(false) }
    val role= sharedViewModel.user.value?.role

    // Show the dialog when the composable is first composed
    LaunchedEffect(Unit) {
        showDialog = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF001F3F), Color(0xFF003366))))
            .padding(1.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.border),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = "",
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = user!!.username,
            style = TextStyle(
                fontFamily = heading,
                fontSize = 30.sp,
                color = Color.White,
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                tokenManager.saveToken(null)
                sharedViewModel.updateUser(null)
                navController.navigate(Navscreen.Landing.route)
            },
            colors = ButtonDefaults.buttonColors(containerColor= Color(0xFF3B5998)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            Text(text = "Logout", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))



        if(role != "admin") {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                ) {
                    TabItem(
                        title = "Performance",
                        color = performace,
                        fontFamily = dancing,
                        modifier = Modifier.weight(1f)
                    ) {
                        navController.navigate("performance") // Replace with actual route
                    }
                    TabItem(
                        title = "Team",
                        color = team,
                        fontFamily = dancing,
                        modifier = Modifier.weight(1f)
                    ) {
                        navController.navigate("team") // Replace with actual route
                    }
                }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Info(user)
    }
}

@Composable
fun TabItem(title: String, color: Color, fontFamily: FontFamily, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
        color = color,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = fontFamily,
                fontSize = 38.sp,
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}







