package com.work.workhubpro.ui.screens.bottombar

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.ui.navigation.Navscreen
import com.work.workhubpro.ui.screens.addemploy.Addemploy
import com.work.workhubpro.ui.screens.chat.Chat
import com.work.workhubpro.ui.screens.community.Community
import com.work.workhubpro.ui.screens.home.Home
import com.work.workhubpro.ui.screens.landing.LandingPage
import com.work.workhubpro.ui.screens.profile.Profile
import com.work.workhubpro.ui.screens.projectDetails.ProjectDetails
import com.work.workhubpro.ui.screens.projectform.CreateProject
import com.work.workhubpro.ui.screens.projects.ProjectListScreens
import com.work.workhubpro.ui.screens.taskform.Create_task

data class BottomnavItems(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val items = listOf(
    BottomnavItems(
        title = "home",
        route = Navscreen.Home.route,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomnavItems(
        title = "projects",
        route = Navscreen.Projects.route,
        selectedIcon = Icons.Filled.Work,
        unselectedIcon = Icons.Outlined.Work,
    ),
    BottomnavItems(
        title = "community",
        route = Navscreen.Community.route,
        selectedIcon = Icons.Filled.Chat,
        unselectedIcon = Icons.Outlined.Chat
    ),
            BottomnavItems(
            title = "profile",
    route = Navscreen.Profile.route,
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person,
)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Bottombar(name: String, navController: NavController,sharedViewModel: SharedViewModel) {

    val navigation = rememberNavController()
    var selectedItemindex by rememberSaveable {
        mutableIntStateOf(0)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navigation.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == item.title } == true,

                        onClick = {
                            selectedItemindex = index
                            navigation.navigate(item.route) {
                                popUpTo(navigation.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {

                                }) {
                                Icon(
                                    imageVector = if (index == selectedItemindex) {
                                        item.selectedIcon
                                    } else item.unselectedIcon, contentDescription = item.title
                                )
                            }
                        })
                }
            }
        },

        ) { paddingValues ->
        NavHost(
            navController = navigation, startDestination = Navscreen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = Navscreen.Home.route) {
                Home(name, navController = navigation,sharedViewModel)
            }
            composable(route = Navscreen.Community.route) {
                Community(navController = navigation,sharedViewModel)
            }
            composable(route = Navscreen.Projects.route) {
                ProjectListScreens(navController = navigation,sharedViewModel)
            }
            composable(route = Navscreen.Profile.route) {
                Profile(navController = navigation,sharedViewModel)
            }
            composable(route = Navscreen.CreateProject.route) {
                CreateProject(navController = navigation,sharedViewModel)
            }
            composable(route = Navscreen.Addempoly.route) {
                Addemploy(navController = navigation, sharedViewModel =sharedViewModel )
            }
            composable(route = Navscreen.Createtask.route) {
                Create_task(navController = navigation,sharedViewModel)
            }
            composable(route = Navscreen.Addempoly.route) {
                Addemploy(navController = navigation,sharedViewModel)
            }
            composable (route = Navscreen.Landing.route) {
                LandingPage(navController = navController,sharedViewModel)
            }
            composable(
                route = "${Navscreen.ProjectDetails.route}/{name}",
                arguments = listOf(navArgument("name") { type = NavType.StringType })
            ) { backStackEntry ->
                val argumentName = backStackEntry.arguments?.getString("name").orEmpty()
                ProjectDetails(argumentName, navController = navController,sharedViewModel)
            }
            composable(
                route = "${Navscreen.Chat.route}/{receiver}",
                arguments = listOf(navArgument("receiver") { type = NavType.StringType })
            ) { backStackEntry ->
                val argumentName = backStackEntry.arguments?.getString("receiver").orEmpty()
                Chat(argumentName, navController = navController,sharedViewModel)
            }
            composable(
                route = "${Navscreen.ProjectDetails.route}/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val argumentName = backStackEntry.arguments?.getString("id").orEmpty()
                ProjectDetails(argumentName, navController = navController,sharedViewModel)
            }

        }
    }
}
