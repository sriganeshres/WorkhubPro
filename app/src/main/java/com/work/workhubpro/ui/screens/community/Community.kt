package com.work.workhubpro.ui.screens.community

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.work.workhubpro.SharedViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.work.workhubpro.models.User
import com.work.workhubpro.ui.navigation.Navscreen
import com.work.workhubpro.ui.screens.chat.Chat
import com.work.workhubpro.ui.theme.LightBlue
import com.work.workhubpro.ui.theme.Lightblue2
import com.work.workhubpro.ui.theme.mediumblue
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@Composable
fun Community(navController: NavController,sharedViewModel: SharedViewModel){
    val userId = sharedViewModel.user.collectAsState().value?.ID
    val workhub_Id = sharedViewModel.user.collectAsState().value?.id
    val communityViewModel: CommunityViewModel = hiltViewModel()
   val projectlead= communityViewModel.project_lead.collectAsState().value
    val employee = communityViewModel.employees.collectAsState().value
    LaunchedEffect(Unit) {
        if (workhub_Id != null) {
            communityViewModel.getProjectLeaders(workhub_Id)
        }
        if (workhub_Id != null) {
            communityViewModel.getemployees(workhub_Id)
        }
    }
    val people = (projectlead.orEmpty()+employee.orEmpty()).toMutableList()


    val dummy = User("ganesh","ejdjowecsecf", "jiejfjef")
    val users:MutableList<User> = mutableListOf<User>(dummy)
    val userNames = people.mapNotNull { user ->
        if (user.id != userId) {
            user.username
        } else {
            null // Skip this user if id matches userId
        }
    }
                Scaffold(
                   // Replace lightBlue with your desired light blue color
                    modifier = Modifier.fillMaxSize(),

                    topBar = {
                        TopAppBar(
                            title = {Text(text = "Employers",
                                onTextLayout = {}) },
                        )
                    }
                ) {
                    Surface(
                        color = LightBlue, // Use the LightBlue color here
                        modifier = Modifier.fillMaxSize()
                    ) {
                        UserList(userNames = userNames, navController)
                    }
                }
            }

@Composable
fun UserList(userNames: List<String>, navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 60.dp)
    ) {
        items(userNames) { userName ->
            UserListItem(userName = userName, navController = navController)
        }
    }
}

@Composable
fun UserListItem(userName: String, navController: NavController) {
       Surface(
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 10.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate("${Navscreen.Chat.route}/$userName")
                },
           color = mediumblue,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(text = userName,
                modifier=Modifier.padding(vertical = 10.dp, horizontal = 5.dp),

                )
        }
}


@Preview
@Composable
fun CommunityPreview() {
    val navController = rememberNavController()
    val sharedViewModel = remember { SharedViewModel() }
    Community(navController = navController, sharedViewModel = sharedViewModel)
}