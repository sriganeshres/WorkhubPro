package com.work.workhubpro.ui.screens.addemploy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.work.workhubpro.models.SendMail
import com.work.workhubpro.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class EmployViewModel @Inject constructor(private val repo : UserRepository):ViewModel(){
    fun addemploy(email:String ,code:Int) {
        val req = SendMail(email,code)
        viewModelScope.launch {
            repo.sendemail(req)
        }
    }
}