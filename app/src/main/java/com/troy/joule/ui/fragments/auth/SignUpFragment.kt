package com.troy.joule.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.troy.joule.JouleApp
import com.troy.joule.Methods.isConnected
import com.troy.joule.R
import com.troy.joule.databinding.FragmentSignUpBinding
import com.troy.joule.repository.Repository
import com.troy.joule.repository.models.User
import com.troy.joule.ui.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: JouleApp
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater,container,false)
        app = requireActivity().application as JouleApp
        repository = app.repository

        binding.btnSignUp.setOnClickListener {

            binding.txtNewFullName.isErrorEnabled = false
            binding.txtNewEmail.isErrorEnabled = false
            binding.txtNewPassword.isErrorEnabled = false

            try {

                val fullName = binding.txtNewFullName.editText!!.text.toString()
                val email = binding.txtNewEmail.editText!!.text.toString()
                val password = binding.txtNewPassword.editText!!.text.toString()

                if(fullName.isNotEmpty()){

                    if(email.isNotEmpty()){

                        if(password.isNotEmpty()){

                            if(isConnected(requireActivity())){

                                val user = User("", fullName, email, password)

                                CoroutineScope(Dispatchers.IO).launch {

                                   if(repository.register(user)){

                                       withContext(Dispatchers.Main) {
                                           repository.getUser().observe(this@SignUpFragment) { list ->
                                               if (!list.isNullOrEmpty()) {
                                                   app.user = list.first()
                                                   Toast.makeText(
                                                       requireContext(),
                                                       "Welcome ${app.user!!.fullName}",
                                                       Toast.LENGTH_LONG
                                                   ).show()
                                                   gotoMain()
                                               } else {
                                                   //Log.d(TAG, "onCreateView: $list")
                                                   Snackbar.make(
                                                       it,
                                                       "Couldn't Sign Up",
                                                       Snackbar.LENGTH_LONG
                                                   ).show()
                                               }
                                           }
                                       }

                                   }else{
                                       Snackbar.make(it, "Invalid Credentials", Snackbar.LENGTH_LONG).show()
                                   }

                                }

                            }else{
                                Snackbar.make(it, "Invalid Credentials", Snackbar.LENGTH_LONG).show()
                            }


                        }else{
                            binding.txtNewPassword.error = "Invalid Password"
                        }

                    }else{
                        binding.txtNewEmail.error = "Invalid Email"
                    }

                }else{
                    binding.txtNewFullName.error = "Invalid Name"
                }


            }catch(e: Exception){
                e.printStackTrace()
            }

        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun gotoMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        requireActivity().startActivity(intent)
        requireActivity().finish()
    }

}