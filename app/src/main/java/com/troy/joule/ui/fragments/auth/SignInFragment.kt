package com.troy.joule.ui.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.troy.joule.JouleApp
import com.troy.joule.Methods.isConnected
import com.troy.joule.databinding.FragmentSignInBinding
import com.troy.joule.repository.Repository
import com.troy.joule.repository.models.User
import com.troy.joule.ui.activities.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private lateinit var app: JouleApp
    private lateinit var repository: Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        app = requireActivity().application as JouleApp
        repository = app.repository

        binding.btnSignIn.setOnClickListener {

            try {
                binding.txtCurrentPassword.isErrorEnabled = false
                binding.txtCurrentEmail.isErrorEnabled = false

                val email = binding.txtCurrentEmail.editText!!.text.toString()
                val password = binding.txtCurrentPassword.editText!!.text.toString()

                if (email.isNotEmpty()) {
                    if (password.isNotEmpty()) {
                        if (isConnected(requireActivity())) {

                            val user = User("", null, email, password)

                            CoroutineScope(Dispatchers.IO).launch {
                                if (repository.login(user)) {

                                    withContext(Dispatchers.Main) {
                                        repository.getUser().observe(this@SignInFragment) { list ->
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
                                                    "Couldn't Sign In",
                                                    Snackbar.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }

                                } else {
                                    Snackbar.make(it, "Invalid Credentials", Snackbar.LENGTH_LONG).show()
                                }
                            }

                        } else {
                            Snackbar.make(it, "Couldn't Connect to Server", Snackbar.LENGTH_LONG).show()
                        }
                    } else {
                        binding.txtCurrentPassword.error = "Invalid Password"
                    }
                } else {
                    binding.txtCurrentEmail.error = "Invalid Email"
                }

            }catch(e: Exception){
                e.printStackTrace()
            }


        }

        binding.btnSignUpPrompt.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            findNavController().navigate(action)
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