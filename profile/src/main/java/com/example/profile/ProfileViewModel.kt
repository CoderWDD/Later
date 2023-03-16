package com.example.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.enums.LoginType
import com.google.firebase.auth.AdditionalUserInfo
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel: ViewModel() {
    private val _loginState = MutableStateFlow(false)
    val loginState = _loginState

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user = _user

    private val _authCredential = MutableStateFlow<AuthCredential?>(null)
    val authCredential = _authCredential

    private val _additionalUserInfo = MutableStateFlow<AdditionalUserInfo?>(null)
    val additionalUserInfo = _additionalUserInfo

    var loginType = LoginType.None

    fun setLoginState(state: Boolean) {
        _loginState.value = state
    }

    fun setUser(user: FirebaseUser?) {
        _user.value = user
    }

    fun setAuthCredential(authCredential: AuthCredential?) {
        _authCredential.value = authCredential
    }

    fun setAdditionalUserInfo(additionalUserInfo: AdditionalUserInfo?) {
        _additionalUserInfo.value = additionalUserInfo
    }

}