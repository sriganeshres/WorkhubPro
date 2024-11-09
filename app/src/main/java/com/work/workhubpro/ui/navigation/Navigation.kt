package com.work.workhubpro.ui.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.ui.screens.bottombar.Bottombar
import com.work.workhubpro.ui.screens.create_Org.CreateOrgScreen
import com.work.workhubpro.ui.screens.home.Home
import com.work.workhubpro.ui.screens.joinOrg.JoinOrganization_Screen
import com.work.workhubpro.ui.screens.landing.LandingPage
import com.work.workhubpro.ui.screens.loginPage.LoginScreen
import com.work.workhubpro.ui.screens.performance.PerformanceScreen
import com.work.workhubpro.ui.screens.profile.Profile
import com.work.workhubpro.ui.screens.projects.ProjectListScreens
import com.work.workhubpro.ui.screens.signup.SignupScreen
import com.work.workhubpro.ui.screens.startingpage.Starting


@Composable
fun Navigation() {
    val navController = rememberNavController()
    val sharedViewModel: SharedViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = Navscreen.Starting.route ){
    composable(route = Navscreen.Signup.route){
     SignupScreen(navController = navController)
    }
    composable(route = Navscreen.Profile.route){
      Profile(navController = navController,sharedViewModel)
    }
        composable(route = Navscreen.Starting.route){
            Starting(navController = navController,sharedViewModel)
        }

        composable(
            route = "${Navscreen.Bottom.route}/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val argumentName = backStackEntry.arguments?.getString("name").orEmpty()
            Bottombar(argumentName, navController = navController,sharedViewModel)
        }
        composable(
            route = "${Navscreen.Home.route}/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) { backStackEntry ->
            val argumentName = backStackEntry.arguments?.getString("name").orEmpty()
            Home(argumentName, navController = navController,sharedViewModel)
        }
        composable(route = Navscreen.Login.route) {
            LoginScreen(navController = navController,sharedViewModel)
        }
        composable(route = Navscreen.Projects.route) {
            ProjectListScreens(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(route = Navscreen.Performance.route) {
            PerformanceScreen(navController = navController, sharedViewModel = sharedViewModel)
        }

        composable(route=Navscreen.Create_Org.route){
            CreateOrgScreen(navController = navController,sharedViewModel)
        }
        composable (route = Navscreen.Landing.route) {
            LandingPage(navController = navController,sharedViewModel)
        }
        composable(route = Navscreen.JoinOrganization_Screen.route) {
            JoinOrganization_Screen(navController = navController, sharedViewModel = sharedViewModel)
        }
    }
}