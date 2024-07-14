package com.task.ecommercebluefunder.repository.auth

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import com.task.ecommercebluefunder.constants.DataStoreKey
import com.task.ecommercebluefunder.state_management.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class AuthRepositoryImp (private val dataStore: DataStore<Preferences> , private val auth:FirebaseAuth) :AuthRepository{
    override suspend fun UpdateLoginStatues(loginStatues:Boolean) {
     dataStore.edit {preferance->
         preferance[DataStoreKey.LOGIN_STATUES]=loginStatues
     }
    }

    override suspend fun getLoginStatues(): Flow<Boolean> {
        val loginStatus: Flow<Boolean> = dataStore.data
            .map { preferences ->
                preferences[DataStoreKey.LOGIN_STATUES] ?: false
            }
        return loginStatus
    }

    override suspend fun LoginWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<Resources<String>> = flow {
        try{
            emit(Resources.Loading())

            val authResult = auth.signInWithEmailAndPassword(email , password).await()
            authResult.user?.let {user->
                Log.d("rehamUID" ,user.uid)
                emit(Resources.Sucess(user.uid))

            }?:run{
                Log.d("rehomuser" , "null")
                emit(Resources.Error(Exception(" user null")))

            }
        }catch ( e:Exception){
            emit(Resources.Error(e))
        }
    }


}

