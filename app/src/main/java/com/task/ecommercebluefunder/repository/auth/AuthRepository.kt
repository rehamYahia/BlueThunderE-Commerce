package com.task.ecommercebluefunder.repository.auth

import com.task.ecommercebluefunder.state_management.Resources
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

  suspend fun UpdateLoginStatues(loginStatues:Boolean)

  suspend fun getLoginStatues(): Flow<Boolean>
  suspend fun LoginWithEmailAndPassword(email:String , password:String):Flow<Resources<String>>
  suspend fun LoginWithGoogle ( idToken: String):Flow<Resources<String>>
}