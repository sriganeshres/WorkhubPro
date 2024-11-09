package com.work.workhubpro.ui.screens.community
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
public class CommunityViewModel @Inject constructor( private val users: UserRepository) : ViewModel() {
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