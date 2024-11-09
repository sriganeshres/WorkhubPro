package com.work.workhubpro.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.work.workhubpro.utils.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
public class ProfileViewModel @Inject constructor(private val tokenManager:TokenManager) : ViewModel() {
    fun getTokenManager(): TokenManager {
        return tokenManager
    }
}
