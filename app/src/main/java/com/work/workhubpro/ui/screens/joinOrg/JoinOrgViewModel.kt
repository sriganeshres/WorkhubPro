package com.work.workhubpro.ui.screens.joinOrg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.workhubpro.models.JoinOrganization
import com.work.workhubpro.models.User
import com.work.workhubpro.repository.JoinOrganizationRepo
import com.work.workhubpro.repository.UserRepository
import com.work.workhubpro.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinOrgViewModel @Inject constructor(
    private val repo: JoinOrganizationRepo,
    private val repo2: UserRepository,
    private val tokenManager: TokenManager
) : ViewModel() {
    val id = repo.id
    val user = repo2.user
    val token = repo2.token
    fun getTokenManger(): TokenManager {
        return tokenManager
    }
    fun joinOrg(email: String, key : Int){
        val newJoinOrganization = JoinOrganization(email, key)
        viewModelScope.launch {
            repo.join(newJoinOrganization)
        }
    }

    fun getOrg(key: Int){
        viewModelScope.launch {
            repo.getID(key)
        }
    }
    fun signupUser(username: String, email: String, password: String, id: Int, role: String) {
        val newUser = User(username, email, password, id, role = role) // Create a new User instance
        viewModelScope.launch {
            repo2.getUser(newUser)
        }
    }
}