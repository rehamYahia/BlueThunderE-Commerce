package com.task.ecommercebluefunder.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.task.ecommercebluefunder.R
import com.task.ecommercebluefunder.databinding.FragmentLoginBinding
import com.task.ecommercebluefunder.state_management.Resources
import com.task.ecommercebluefunder.ui.common.views.ProgressDialog
import com.task.ecommercebluefunder.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class LoginFragment : Fragment() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel : AuthViewModel by viewModels()
    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // Set up sign-in button

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.loginViewmodel = authViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.registerTv.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.googleSigninBtn.setOnClickListener {
            signIn()
        }
        initViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun initViewModel(){
        lifecycleScope.launch {
         authViewModel.auth_state.collect{state->
             state?.let{resource ->
                 when(resource){
                     is Resources.Loading ->{
                         progressDialog.show()
                     }
                     is Resources.Sucess ->{
                         progressDialog.dismiss()
                         Toast.makeText( requireContext(), "Sucess" +resource.data, Toast.LENGTH_LONG).show()


                     }
                     is Resources.Error ->{
                         progressDialog.dismiss()
                         Toast.makeText( requireContext(), resource.exception?.message , Toast.LENGTH_LONG).show()
                     }
                 }
             }
         }
        }
    }


    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            val displayName = account.displayName
            val email = account.email
            val profilePicUrl = account.photoUrl
        } else {
            // Sign-in failed
        }
    }

    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        updateUI(account)
    }



}