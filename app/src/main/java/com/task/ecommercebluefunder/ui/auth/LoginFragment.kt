package com.task.ecommercebluefunder.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.task.ecommercebluefunder.BuildConfig
import com.task.ecommercebluefunder.R
import com.task.ecommercebluefunder.databinding.FragmentLoginBinding
import com.task.ecommercebluefunder.state_management.Resources
import com.task.ecommercebluefunder.ui.common.views.ProgressDialog
import com.task.ecommercebluefunder.utils.getGoogleRequestIntent
import com.task.ecommercebluefunder.utils.showSnakeBarError
import com.task.ecommercebluefunder.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val authViewModel : AuthViewModel by viewModels()
    val progressDialog by lazy { ProgressDialog.createProgressDialog(requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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



}