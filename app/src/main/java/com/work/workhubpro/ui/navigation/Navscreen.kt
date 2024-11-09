package com.work.workhubpro.ui.navigation

sealed class Navscreen(val route: String) {
    object Signup : Navscreen("signup")
    object Profile : Navscreen("profile")
    object Bottom : Navscreen("bottom")
    object Home : Navscreen("home")
    object Addempoly : Navscreen("Addemploy")

    object Community : Navscreen("community")
    object Work : Navscreen("work")
    object Projects : Navscreen("projects")
    object Landing : Navscreen("landing")
    object Create_Org : Navscreen ("create_org")
    object Chat : Navscreen("chat")
    object Login : Navscreen("login")
    object ProjectDetails : Navscreen("project_details")
    object JoinOrganization_Screen :Navscreen("join")
    object CreateProject: Navscreen("create_project")
    object Createtask: Navscreen("create_task")
    object Starting: Navscreen("starting")
    object Performance: Navscreen("performance")
    object Team: Navscreen("team")



}