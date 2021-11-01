package com.troy.joule.ui.fragments.main.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troy.joule.repository.Repository

class SharedViewModelFactory():  ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SharedViewModel() as T
    }
}