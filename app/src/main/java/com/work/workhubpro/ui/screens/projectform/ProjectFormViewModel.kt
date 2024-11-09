package com.work.workhubpro.ui.screens.projectform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.workhubpro.SharedViewModel
import com.work.workhubpro.models.Project
import com.work.workhubpro.models.User
import com.work.workhubpro.repository.Projectcreation
import com.work.workhubpro.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
public class ProjectFormmViewMoel @Inject constructor(private val repo: Projectcreation, private val users: UserRepository) : ViewModel() {
    fun createProject(name: String, description: String, projectlead: String, employees:List<User>, workhubid:Int) {
        println(name)
        val project = Project(name, description, projectLead = projectlead, Members = employees, workHub_id = workhubid)    // Create a new User instance
        viewModelScope.launch {
            repo.getProject(project)
        }
    }
    val project_lead=users.projectleaders
    val employees=users.employees
     fun getProjectLeaders(workhub_id:Int){
        viewModelScope.launch {
            users.projectLead_from_workhub(workhub_id)
        }
    }
     fun getemployees(workhub_id:Int){
        viewModelScope.launch {
            users.employees_from_workhub(workhub_id)
        }
    }
}