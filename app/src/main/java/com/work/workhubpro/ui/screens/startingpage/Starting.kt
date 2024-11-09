package com.work.workhubpro.ui.screens.startingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.work.workhubpro.R
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.ui.navigation.Navscreen
import com.work.workhubpro.ui.screens.landing.LandingViewModel

@Composable
fun Starting(navController: NavController,sharedViewModel:SharedViewModel)
{
    val ViewModel : StartingViewModel = hiltViewModel()
    val user = ViewModel.user.collectAsState().value
    val tokenManager = ViewModel.getTokenManager()
    val success = ViewModel.success.collectAsState().value
    println("printing toke value")
    println(tokenManager.getToken())
    if(tokenManager.getToken()!=null){
        println(tokenManager.getToken())
        ViewModel.user_from_token(tokenManager.getToken()!!)
        LaunchedEffect(success) {
            if(success!=null) {
                println("false")
                if (success==true) {
                    println(user)
                    println(user?.username)
                    sharedViewModel.updateUser(user)
                    println("hello")
                    navController.navigate(
                        Navscreen.Bottom.route + "/${
                            user?.username?.substring(
                                0,
                                4
                            )
                        }"
                    )
                } else {
                    println("trivedi")
                    navController.navigate(Navscreen.Landing.route)
                }

            }

        }
    }
    else{
        navController.navigate(Navscreen.Landing.route)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(
            painter = painterResource(id = R.drawable.logopage),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}