package com.troy.joule.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.troy.joule.JouleApp
import com.troy.joule.R
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModel
import com.troy.joule.ui.fragments.main.viewModel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =

        setContentView(R.layout.activity_main)

        val app = (application as JouleApp)
        val repository = app.repository
        val viewModelFactory = SharedViewModelFactory()
        sharedViewModel = ViewModelProvider(this, viewModelFactory).get(
            SharedViewModel::class.java)

        sharedViewModel.init(repository)
    }
}