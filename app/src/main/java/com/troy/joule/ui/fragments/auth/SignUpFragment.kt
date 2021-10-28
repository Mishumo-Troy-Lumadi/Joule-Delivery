package com.troy.joule.ui.fragments.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.troy.joule.JouleApp
import com.troy.joule.R
import com.troy.joule.databinding.FragmentSignUpBinding
import com.troy.joule.repository.Repository

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

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}