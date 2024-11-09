package com.work.workhubpro.ui.screens.projectDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.workhubpro.models.Project
import com.work.workhubpro.repository.Projectcreation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailsViewModel @Inject constructor(private val repo: Projectcreation): ViewModel(){
    val project = repo.currProject
    fun getProjectDetails(request :Int) {
        println("hello killpin pandey")
        viewModelScope.launch {
            repo.getProjectDetails(request)
        }
    }
}