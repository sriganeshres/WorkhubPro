package com.work.workhubpro

import androidx.lifecycle.ViewModel
import com.work.workhubpro.models.Organisation
import com.work.workhubpro.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
public class SharedViewModel  @Inject constructor(): ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?>  = _user.asStateFlow()

    private val _workhub = MutableStateFlow<Organisation?>(null)
    val workhub: StateFlow<Organisation?>  = _workhub.asStateFlow()

    fun updateUser(newUser: User?) {
        _user.value = newUser
    }

    fun updateWorkhub(newWorkhub: Organisation) {
        _workhub.value = newWorkhub
    }

}
