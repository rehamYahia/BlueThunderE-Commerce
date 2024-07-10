package com.task.ecommercebluefunder.di


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.task.ecommercebluefunder.repository.auth.AuthRepository
import dagger.Provides
import com.task.ecommercebluefunder.repository.auth.AuthRepositoryImp
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthRepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth():FirebaseAuth{
        return  FirebaseAuth.getInstance()
    }
    @Provides
    @Singleton
    fun provideAuthRepo(authdataStore: DataStore<Preferences> , auth:FirebaseAuth): AuthRepository {

        return AuthRepositoryImp(authdataStore,auth)
    }

}