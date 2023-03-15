package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel: ViewModel() {
    private val _loginState = MutableStateFlow(false)
    val loginState = _loginState

    fun setLoginState(state: Boolean) {
        _loginState.value = state
    }
}