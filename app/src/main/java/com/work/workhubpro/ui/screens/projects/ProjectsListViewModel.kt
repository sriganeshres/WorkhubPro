package com.work.workhubpro.ui.screens.projects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.workhubpro.repository.Projectcreation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsListViewModel @Inject constructor(private val repo: Projectcreation): ViewModel(){
    val projects = repo.projects
    fun getallprojects(id: String) {
        viewModelScope.launch {
            repo.getallProjects(id)
        }
    }
}