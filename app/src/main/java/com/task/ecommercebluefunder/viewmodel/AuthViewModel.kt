package com.task.ecommercebluefunder.viewmodel

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.ecommercebluefunder.repository.auth.AuthRepository
import com.task.ecommercebluefunder.state_management.Resources
import com.task.ecommercebluefunder.utils.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _LOGIN_STATUES: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val LOGIN_STATUES get() = _LOGIN_STATUES

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val auth_state: MutableSharedFlow<Resources<String>?> = MutableSharedFlow()
    val loginValidation: Flow<Boolean> = combine(email, password) { email, password ->
        email.isValidEmail() && password.length >= 6
    }



    init {
        viewModelScope.launch {
            updateLoginState(false)
            getLoginStatues()
        }
    }

    private suspend fun getLoginStatues() {
        viewModelScope.launch {
            authRepository.getLoginStatues().collect {
                _LOGIN_STATUES.value = it
            }
        }
    }

    private suspend fun updateLoginState(state: Boolean) {
        viewModelScope.launch {
            authRepository.UpdateLoginStatues(state)
        }
    }

    fun login(){
        viewModelScope.launch {
            val email = email.value
            val password = password.value
            if(loginValidation.first()){
                authRepository.LoginWithEmailAndPassword(email , password).onEach {resource->
                    when(resource){
                    is Resources.Loading -> {
                        auth_state.emit (Resources.Loading() )
                    }
                    is Resources.Sucess ->{
                        auth_state.emit( Resources.Sucess("") )
                    }
                    is Resources.Error ->{
                        auth_state.emit(Resources.Error(resource.exception ?: Exception("oppppps")) )
                    }
                }
                }.launchIn(viewModelScope)
            }else{
                auth_state.emit(Resources.Error(Exception("Invalid email or password")) )
            }
        }
    }
}